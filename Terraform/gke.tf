resource "google_compute_subnetwork" "subnet"{
    name = "my-subnet"
    ip_cidr_range="10.2.204.0/28"
    region= var.region
    network = "default"
    private_ip_google_access=true
    secondary_ip_range{
        range_name="alias-range"
        ip_cidr_range="10.10.0.0/16"
        
    }
  
}
resource "google_compute_address" "instance_ip" {
    name="instance-ip"
    region= var.region
    
   
}

variable "gke_num_nodes" {
  default     = 2
  description = "number of gke nodes"
}

resource "google_container_cluster" "cluster" {
  name     = "${var.project_id}-gke"
  location = var.zone

 
  remove_default_node_pool = true
  initial_node_count       = 3
  network ="default"
  subnetwork= google_compute_subnetwork.subnet.id


  private_cluster_config{
    enable_private_nodes=true
    enable_private_endpoint=false
    master_ipv4_cidr_block="10.2.208.0/28"
    
    
  }
  ip_allocation_policy{

  }
  

}

resource "google_compute_instance" "instance"{
    name= "vm-carlos"
    machine_type="e2-micro"
    zone= var.zone

    boot_disk{
        initialize_params{
            image="debian-cloud/debian-10"
        }
    }

    network_interface{
        network="default"
        subnetwork= google_compute_subnetwork.subnet.id
        access_config{
            nat_ip=google_compute_address.instance_ip.address
            network_tier= "PREMIUM"
        }
    }

    metadata_startup_script= <<-EOF
        sudo apt-get update
        sudo apt-get install -y tinyproxy
        sudo sed -i '#/#Port 8888/Port 8888/g' /etc/tinyproxy/tinyproxy.conf
        sudo sed -i '#/#Allow 127.0.0.1/Allow 127.0.0.1\nAllow localhost/g' /etc/tinyproxy/tinyproxy.conf
        sudo service tinyproxy restart
    EOF
        
}
# Separately Managed Node Pool

resource "google_container_node_pool" "nodes" {
  name       = google_container_cluster.cluster.name
  location   = var.zone
  cluster    = google_container_cluster.cluster.name
  node_count = var.gke_num_nodes

  node_config {
    oauth_scopes = [
        "https://www.googleapis.com/auth/devstorage.read_only",
        "https://www.googleapis.com/auth/logging.write",
        "https://www.googleapis.com/auth/monitoring",
        "https://www.googleapis.com/auth/service.management.readonly",
        "https://www.googleapis.com/auth/servicecontrol",
        "https://www.googleapis.com/auth/trace.append",
    ]

    labels = {
      env = var.project_id
    }
  }
}


