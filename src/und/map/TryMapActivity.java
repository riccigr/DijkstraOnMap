package und.map;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class TryMapActivity extends Activity {
	
	String [] stateArray = new String[]{"Aracaju",
			"Belém",
			"Belo Horizonte",
			"Boa Vista",
			"Campo Grande",
			"Cuiabá", 
			"Curitiba",
			"Florianópolis", 
			"Fortaleza", 
			"Goiânia", 
			"João Pessoa", 
			"Macapá", 
			"Maceió", 
			"Manaus", 
			"Natal", 
			"Palmas", 
			"Porto Alegre", 
			"Porto Velho",
			"Recife",
			"Rio Branco", 
			"Rio de Janeiro",
			"Salvador", 
			"São Luís", 
			"São Paulo", 
			"Teresina",
			"Vitória"};
	
	static  List<Vertex> vertices;
	static  List<Edge> arcos;
	ArrayList<String> listProducts = new ArrayList<String>();	//-- produtos cadastrados
	List<Vertex> listagemDestino = new ArrayList<Vertex>();		//-- cidades de destino
	EditText novo_produto;			//-- produto a ser adicionado
	int inicio;						//-- valor da cidade origem
	int fim;						//-- valor da cidade destino
	
	//-- Tela Principal
	void screenMain(){
		setContentView(R.layout.first);
		
		Button btnMount = (Button) findViewById(R.btn.main_mount);
		Button btnCadProd = (Button) findViewById(R.btn.main_cadprod);
		
		btnMount.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				screenMount();
			}
		});
		
		btnCadProd.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				screenProduct();
			}
		});
	}
		
	//montagem de carga
	void screenMount(){
		setContentView(R.layout.mount);
		
		Spinner stateSpinner = (Spinner) findViewById(R.combo.destino);
		Spinner productSpinner = (Spinner) findViewById(R.combo.produto);
		ImageButton btnSair = (ImageButton) findViewById(R.btn.sair);
		ImageButton btnOk = (ImageButton) findViewById(R.btn.ok);
		
		ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		
		//-- carregar a lista de cidades
		stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stateSpinner.setAdapter(stateAdapter);
		for (int i = 0; i < stateArray.length; i++){
			stateAdapter.add(stateArray[i]);
		}
		stateSpinner.setOnItemSelectedListener( new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(TryMapActivity.this, "ok = " + position, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		//-- escolha da cidade origem
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Escolha a Cidade de Origem");
		builder.setSingleChoiceItems(stateArray, 0, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        Toast.makeText(getApplicationContext(), stateArray[item], Toast.LENGTH_SHORT).show();
		        inicio = item;
		        dialog.cancel();
		    }
		});
		builder.setCancelable(false)
	       .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	                screenMain();
	           }
	       });
		AlertDialog alert = builder.create();
		alert.show();
		
		
		//-- carregar lista de produtos
		productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productSpinner.setAdapter(productAdapter);
		if (!listProducts.isEmpty()){
			for (int i = 0; i < listProducts.size(); i++){
				productAdapter.add(listProducts.get(i));
			}
			}else{
		//		Toast.makeText(TryMapActivity.this, "Nenhum Produto Cadastrado", Toast.LENGTH_SHORT).show();
		}

		
		
		btnSair.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				screenMain();
			}
		});
		
		btnOk.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createEnv();
				listagemDestino.add(vertices.get(fim));
				screenCalc();
			}
		});
	}
	
	//-- cadastrar produtos
	void screenProduct(){
		setContentView(R.layout.product);
		
		ImageButton btnSair = (ImageButton) findViewById(R.btn.sair);
		ImageButton btnOk = (ImageButton) findViewById(R.btn.ok);
		novo_produto = (EditText) findViewById(R.campo.newProd);
		
		btnOk.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listProducts.add(novo_produto.getText().toString());
				Toast.makeText(TryMapActivity.this, listProducts.toString(), Toast.LENGTH_SHORT).show();
				novo_produto.setText("");
			}
		});
		
		btnSair.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				screenMain();
			}
		});
	}

	//-- calculo da rota e saida de resultado
	void screenCalc(){
		
		vertices = new ArrayList<Vertex>();
		arcos = new ArrayList<Edge>();
		String route = "";	
		
		createEnv();

		Graph graph = new Graph(vertices, arcos);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		//-- pegar o valor selecionado na primeira lista.
		//-- apenas um teste abaixo
		dijkstra.execute(vertices.get(0));
		
		//-- receber numa lista todos os destinos
		//-- apenas um teste abaixo
		
//		listagem.add(vertices.get(5));
//		listagem.add(vertices.get(10));
//		listagem.add(vertices.get(7));
//		listagem.add(vertices.get(9));
		
		String linha = vertices.get(0).getName();
		while(!listagemDestino.isEmpty()){
			int menor = Integer.MAX_VALUE;
			int temp = 0;
			Vertex target = null;
			
			for (Vertex v : listagemDestino){
				temp = dijkstra.printDistancia(v);
				if (temp < menor){
					menor = temp;
					target = v;
				}
			}
			if (target != null){
			linha += " -> " + target.getName();
			listagemDestino.remove(target);
			dijkstra.execute(target);
			}
		}
		System.out.println(linha);
	}
	
	//-- criacao de arcos e vertices
	void createEnv(){
		for (int i = 0; i < 11; i++) {
			Vertex estado = new Vertex(stateArray[i] + "_" + i, stateArray[i]);
			vertices.add(estado);
		}
		
		addArco("Edge_0", 0, 1, 85);
		addArco("Edge_1", 0, 2, 517);
		addArco("Edge_2", 0, 4, 173);
		addArco("Edge_3", 2, 6, 186);
		addArco("Edge_4", 2, 7, 103);
		addArco("Edge_5", 3, 7, 183);
		addArco("Edge_6", 5, 8, 250);
		addArco("Edge_7", 8, 9, 84);
		addArco("Edge_8", 7, 9, 167);
		addArco("Edge_9", 4, 9, 502);
		addArco("Edge_10", 9, 10, 40);
		addArco("Edge_11", 1, 10, 600);

	}
	
	//-- criacao dos arcos
	void addArco(String id, int inicio, int fim,	int peso) {
		Edge lane = new Edge(id,vertices.get(inicio), vertices.get(fim), peso );
		Edge volta = new Edge(id,vertices.get(fim), vertices.get(inicio), peso );
		arcos.add(lane);
		arcos.add(volta);
	}
	
	//-- chamada principal
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenMain();
    }
    
}