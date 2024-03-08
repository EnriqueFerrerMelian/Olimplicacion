package com.example.olimplicacion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.olimplicacion.databinding.ActivityMainBinding;
import com.example.olimplicacion.db.DataBaseManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    GoogleSignInClient signInClient;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()== RESULT_OK){
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try{
                    GoogleSignInAccount googleSignInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //si la conexión procede, podemos acceder solo al nombre y la imagen de perfil por un tema de permisos
                                auth = FirebaseAuth.getInstance();
                                openActivity2();
                                Toast.makeText(MainActivity.this, "Acceso concedido!", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Fallo al acceder: " + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }catch(ApiException e){
                    e.printStackTrace();
                }

            }
        }
    });

    ActivityMainBinding binding;
    DataBaseManager dbm;
    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .build();
        signInClient = GoogleSignIn.getClient(MainActivity.this, options);
        auth = FirebaseAuth.getInstance();


        //declaracion de variables
        dbm = new DataBaseManager(this);

        //listeners
        binding.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.nombre.getText().toString().equals("admin") && binding.clave.getText().toString().equals("admin")){
                    openActivity2();
                    binding.nombre.setText("");
                    binding.clave.setText("");
                }
            }
        });

        binding.buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = signInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });
    }


    public void openActivity2(){
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
    }
}