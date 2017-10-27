package com.example.karin.todolist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button botaoAdicionar;
    private ListView listView;
    private SQLiteDatabase bancoDados;

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //recuperar dados do xml
            editText = (EditText) findViewById(R.id.editId);
            botaoAdicionar = (Button) findViewById(R.id.buttonId);


            //banco de dados
            //NOME DO BANCO DE DADOS E MODO PRIVADO
            bancoDados = openOrCreateDatabase("apptarefas", MODE_PRIVATE, null);

            //criação de tabelas
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR)");

            //evento clicar no botao
            botaoAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String textoDigitado = editText.getText().toString();
                    salvarTarefa(textoDigitado);

                }
            });

            //chamar o metodo listar tarefas
            recuperarTarefas();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void salvarTarefa(String texto) {

        try{
            if(texto.equals("")){
                Toast.makeText(MainActivity.this,"Digite uma tarefa", Toast.LENGTH_SHORT).show();
            }else{
                bancoDados.execSQL("INSERT INTO tarefas (tarefa) VALUES('" + texto + "')");
                Toast.makeText(MainActivity.this,"Tarefa salva com sucesso",Toast.LENGTH_SHORT).show();
                recuperarTarefas();
                editText.setText("");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void recuperarTarefas(){
        try{
            //cursor para manipular registros que foram retornados, recupera tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);

            //recupera ids das colunas
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            //Lista
            listView = (ListView) findViewById(R.id.listId);

            //Criar o adaptador
            itens = new ArrayList<String>();
            itensAdaptador = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,itens);
            listView.setAdapter(itensAdaptador);

            //listar as tarefas
            cursor.moveToFirst();

            while (cursor != null) {

                Log.i("Resultado - ", "Tarefa: " + cursor.getString(indiceColunaTarefa));
                itens.add(cursor.getString(indiceColunaTarefa));
                cursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


