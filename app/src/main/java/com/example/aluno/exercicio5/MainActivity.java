package com.example.aluno.exercicio5;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.content.Context;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.URL;
        import java.io.UnsupportedEncodingException;
        import java.io.InputStreamReader;
        import java.io.Reader;
        import java.net.HttpURLConnection;
        import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {


    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consulta();
            }
        });

    }



    public void consulta() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo redeInfo = connMgr.getActiveNetworkInfo();
        if (redeInfo != null && redeInfo.isConnected()) {
            new ComunicacaoAssincrona().execute("https://goo.gl/g9hXoa");
        } else {
            Toast.makeText(this,"Erro de rede", Toast.LENGTH_SHORT).show();
        }
    }
    private class ComunicacaoAssincrona extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return realizaRequisicaoHTTP(urls[0]);
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String retorno) {
            ((TextView)findViewById(R.id.idMsg)).setText(retorno);
        }
    }//fim da classe interna ComunicacaoAssincrona
    // Realiza a conexão HTTP e retorna a resposta em String
    private String realizaRequisicaoHTTP(String endereco) throws IOException {
        InputStream is = null;
        int len = 500;
        try {
// A classe URL representa um "Uniform Resource Locator", um ponteiro para um recurso na Web
            URL url = new URL(endereco); // cria um novo objeto que representa um endereço de Web
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Retorna uma instância URLConnection que representa uma conexão com o objeto remoto referido pelo URL.
            conn.setRequestMethod("GET");
            conn.connect(); // Estabelece a conexão HTTP
            is = conn.getInputStream();
            return converteStreamEmString(is, len); // Converte a resposta em string
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    // recebe um InputStream e retorna uma String
    public String converteStreamEmString(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "ISO-8859-1");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
