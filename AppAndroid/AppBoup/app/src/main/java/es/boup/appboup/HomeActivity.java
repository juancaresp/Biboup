package es.boup.appboup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private TextView tvCorreo, tvProveedor;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{
            tvCorreo = findViewById(R.id.tvCorreo);
            tvProveedor = findViewById(R.id.tvProvedor);
            tvCorreo.setText(user.getEmail());
            tvProveedor.setText(user.getProviderId());
        }
    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        onBackPressed();
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idOpcion = item.getItemId();

        switch (idOpcion){
            case R.id.itmLogOut:
                FirebaseAuth.getInstance().signOut();
                onBackPressed();
                break;
            default:
                finishAffinity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}