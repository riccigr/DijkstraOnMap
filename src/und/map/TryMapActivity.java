package und.map;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
//	List<Vertex> listagemDestino = new ArrayList<Vertex>();		//-- cidades de destino
	HashMap<Vertex, String> listagemDestino = new HashMap<Vertex, String>();
	EditText novo_produto, txtOrig;			//-- produto a ser adicionado //-- cidade origem
	String txOrigem = "";
	int inicio = -10;						//-- valor da cidade origem
	int fim = 0;							//-- valor da cidade destino
	int productChoosed = 0;				//-- item escolhido para a viagem.
	boolean isEnvOk = false;
	
	
	// -- ################################ SCREENS ################################

	//-- Tela Principal
	void screenMain(){
		setContentView(R.layout.first);
	
	//	Button btnMount = (Button) findViewById(R.btn.main_mount);
	//	Button btnCadProd = (Button) findViewById(R.btn.main_cadprod);
		ImageView btnMount = (ImageView) findViewById(R.btn.cm);
		ImageView btnCadProd = (ImageView) findViewById(R.btn.cx2);
		
		btnMount.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listProducts.isEmpty()){
					noProductDialog();
				}else{
					inicio = -10;
					fim = 0;
					productChoosed = 0;
					listagemDestino.clear();
					screenMount();
				}
			}
		});
		
		btnCadProd.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				screenProduct();
			}
		});
	}
		
	//-- Montagem de carga
	void screenMount(){
		setContentView(R.layout.mount);
		
		Spinner stateSpinner = (Spinner) findViewById(R.combo.destino);
		Spinner productSpinner = (Spinner) findViewById(R.combo.produto);
		ImageButton btnSair = (ImageButton) findViewById(R.btn.sair);
		ImageButton btnOk = (ImageButton) findViewById(R.btn.ok);
		ImageButton btnInfo = (ImageButton) findViewById(R.btn.info);
		ImageButton btnDel = (ImageButton) findViewById(R.btn.del);
		txtOrig = (EditText) findViewById(R.campo.origro);
		
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
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				fim = position;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		stateSpinner.setSelection(fim);
		
		//-- escolha da cidade origem
		if (inicio < 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Escolha a Cidade de Origem");
			builder.setSingleChoiceItems(stateArray, 0, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        Toast.makeText(getApplicationContext(), stateArray[item], Toast.LENGTH_SHORT).show();
			        inicio = item;
			        txOrigem = stateArray[item];
			        txtOrig.setText(txOrigem);
			        txtOrig.setInputType(0);
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
		}else{
	        txtOrig.setText(vertices.get(inicio).getName());
	        txtOrig.setInputType(0);
	  	    Toast.makeText(TryMapActivity.this, "Insira um novo destino e sua nota fiscal!", Toast.LENGTH_SHORT).show();
		}
		
		//-- carregar lista de produtos
		productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productSpinner.setAdapter(productAdapter);
		if (!listProducts.isEmpty()){
			for (int i = 0; i < listProducts.size(); i++){
				productAdapter.add(listProducts.get(i));
			}
		}
		productSpinner.setOnItemSelectedListener( new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				productChoosed = position;			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		btnSair.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				screenMain();
			}
		});
		productSpinner.setSelection(productChoosed);
		
		//setandos os destinos escolhidos
		btnOk.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isEnvOk){
					createEnv();
				}
				listagemDestino.put(vertices.get(fim), listProducts.get(productChoosed));
				newItemDialog();
			}
		});
		
		btnInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showInfo(1);
			}
		});
		
		btnDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!listagemDestino.isEmpty()){
					int sizeList = listagemDestino.size();
					int tmpCont = sizeList;
					final String[] tmpArray = new String[sizeList];
					final ArrayList<Object> tmpRemoveId = new ArrayList<Object>();

					final boolean[] boolArray = null;
					for (Vertex prod : listagemDestino.keySet()){
						tmpArray[sizeList-tmpCont] = prod.getName();
						tmpCont -= 1;
					}
					
					AlertDialog.Builder builder = new AlertDialog.Builder(TryMapActivity.this);
					builder.setTitle("Escolha os destinos a remover");
					builder.setMultiChoiceItems(tmpArray, boolArray, new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							if(isChecked){
								tmpRemoveId.add(tmpArray[which]);
							}else{
								tmpRemoveId.remove(tmpArray[which]);
							}
						}
					});
					builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   if (!tmpRemoveId.isEmpty()){	
				        	   for(Object idRomve : tmpRemoveId){
				        		   if(listagemDestino.containsKey( idRomve )){
				        		   		listagemDestino.remove(idRomve);
				        	   //	   listagemDestino.remove(getKeyFromValue(listagemDestino,idRomve));
				        		   }else{
				        			   for(Vertex opc : listagemDestino.keySet()){
				        				   System.out.println("Selecionado = " + idRomve);
				        				   System.out.println(opc.getName());
				        			   }
				        		   }
				        	   }
				        	   }
				        	   Toast.makeText(getApplicationContext(), "Destinos removidos com sucesso!", Toast.LENGTH_SHORT).show();
				           }
				       });
					builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       });
				AlertDialog alerta = builder.create();
				alerta.show();

				}else{
			        Toast.makeText(getApplicationContext(), "Não há destinos registradas. \nAdicione um.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	//-- Cadastrar produtos
	void screenProduct(){
		setContentView(R.layout.product);
		
		ImageButton btnSair = (ImageButton) findViewById(R.btn.sair);
		ImageButton btnOk = (ImageButton) findViewById(R.btn.ok);
		ImageButton btnDel = (ImageButton) findViewById(R.btn.del);
		ImageButton btnInf = (ImageButton) findViewById(R.btn.info);
		novo_produto = (EditText) findViewById(R.campo.newProd);
		
		btnOk.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(novo_produto.getText().toString().trim().length() != 0 && novo_produto.getText().toString() != null){
					listProducts.add(novo_produto.getText().toString());
					novo_produto.setText("");
					newProdDialog();
				}else{
		        	Toast.makeText(TryMapActivity.this, "Digite o numero para a nota fiscal!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnInf.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View vi) {
				showInfo(3);
			}
		});
		
		btnSair.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				screenMain();
			}
		});
		
		
		btnDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!listProducts.isEmpty()){
					int sizeList = listProducts.size();
					int tmpCont = sizeList;
					final String[] tmpArray = new String[sizeList];
					final ArrayList<String> tmpRemoveId = new ArrayList<String>();

					final boolean[] boolArray = null;
					for (String prod : listProducts){
						tmpArray[sizeList-tmpCont] = prod;
						tmpCont -= 1;
					}
					
					AlertDialog.Builder builder = new AlertDialog.Builder(TryMapActivity.this);
					builder.setTitle("Escolha as notas fiscais a remover");
					builder.setMultiChoiceItems(tmpArray, boolArray, new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							if(isChecked){
								tmpRemoveId.add(tmpArray[which]);
							}else{
								tmpRemoveId.remove(tmpArray[which]);
							}
						}
					});
					builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   if (!tmpRemoveId.isEmpty()){	
				        	   for(String idRomve : tmpRemoveId){
				        	   		if (listProducts.contains(idRomve)){
				        	   			listProducts.remove(idRomve);
				        	   		}
				        	   	}
				        	   }
				        	   Toast.makeText(getApplicationContext(), "notas ficais removidas com Sucesso!", Toast.LENGTH_SHORT).show();
				           }
				       });
					builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       });
				AlertDialog alerta = builder.create();
				alerta.show();

				}else{
			        Toast.makeText(getApplicationContext(), "Não há notas ficais registradas. \nRegistre uma.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

	//-- calculo da rota e saida de resultado
	void screenCalc(){
		setContentView(R.layout.report);
		
//		vertices = new ArrayList<Vertex>();
//		arcos = new ArrayList<Edge>();
		String route = "", rptDestinos = "", rptProdutos = "";
		int cont = 1, rptDistancia = 0;
		TextView txtDest = (TextView) findViewById(R.campo.rptDest);
		TextView txtProd = (TextView) findViewById(R.campo.rptProd);
		TextView txtDist = (TextView) findViewById(R.campo.rptDist);
		ImageButton btnMain = (ImageButton) findViewById(R.btn.sair);
		ImageButton btnInfo = (ImageButton) findViewById(R.btn.info);
		
		//-- inicia algoritmo
		Graph graph = new Graph(vertices, arcos);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(vertices.get(inicio));
		
		//-- inicializa variaveis de exibiçao
		String linha = vertices.get(inicio).getName();
		rptDestinos = "Origem: " + vertices.get(inicio).getName() + "\n";
		
		//-- ordena pela melhor rota
		while(!listagemDestino.isEmpty()){
			int menor = Integer.MAX_VALUE, temp = 0 ;
			Vertex target = null;
			
			for (Vertex v : listagemDestino.keySet()){
				temp = dijkstra.printDistancia(v);
				if (temp < menor){
					menor = temp;
					target = v;
				}
			}
			if (target != null){
				linha += " -> " + target.getName();
				rptDestinos += cont + "º Destino: " + target.getName() + "\n";
				rptProdutos += cont + "ª Nota Fical: " + listagemDestino.get(target) + "\n";
				rptDistancia += dijkstra.printDistancia(target);
				cont++;
				listagemDestino.remove(target);
				dijkstra.execute(target);
			}
		}
		System.out.println(linha);
		txtDest.setText(rptDestinos);
		txtProd.setText(rptProdutos);
		txtDist.setText(rptDistancia + " Km");
		
		
		btnMain.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				screenMain();
			}
		});
		
		btnInfo.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showInfo(4);
			}
		});
	}
	
	//-- tela de confirmação
	void screenConfirm(){
		setContentView(R.layout.confirm);
		
		ImageButton btnOk = (ImageButton) findViewById(R.btn.confirm);
		ImageButton btnSair = (ImageButton) findViewById(R.btn.voltarmain);
		ImageButton btnInfo = (ImageButton) findViewById(R.btn.info);
//		EditText txtIdent = (EditText) findViewById(R.campo.editIdent);
		TextView editDest = (TextView) findViewById(R.campo.editDest);
		Spinner origSpinner = (Spinner) findViewById(R.combo.spinOrig);
		ArrayAdapter<String> origAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		String txtDest = "";
	
		//-- setando identificacao
//		txtIdent.setText(android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()));
		
		//-- carregando a cidade origem
		origAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		origSpinner.setAdapter(origAdapter);
		for (int i = 0; i < stateArray.length; i++){
			origAdapter.add(stateArray[i]);
		}
		origSpinner.setSelection(inicio);
		origSpinner.setOnItemSelectedListener( new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				inicio = position;
				txOrigem = stateArray[position];
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		//-- carregando os destinos
		if (!listagemDestino.isEmpty()){
			for ( Vertex dest : listagemDestino.keySet()){
				txtDest += dest.getName().toString() + " ( " + listagemDestino.get(dest)  +" ) " + "\n";
			}
		}
		editDest.setText(txtDest);
		
		
		btnSair.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				backConfirmDialog();
			}
		});
		
		btnOk.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				screenCalc();
			}
		});
		
		btnInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showInfo(2);
			}
		});
	}
	
	// -- ################################ DIALOGS ################################
	//-- cria Dialogs quando não a produtos
	void noProductDialog(){
		AlertDialog.Builder builderMsg = new AlertDialog.Builder(TryMapActivity.this);
		
		builderMsg.setTitle("Não há notas fiscais registradas");
		builderMsg.setMessage("Deseja registrar notas fiscais agora?");
		builderMsg.setPositiveButton("Sim", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				 screenProduct();
			}
		});
		builderMsg.setNegativeButton("Não", new  AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 screenMain();
			}
		});
		AlertDialog alertMsg = builderMsg.create();
		alertMsg.setTitle("Não há notas fiscais cadastradas");
		alertMsg.show();
		
	}
	
	//-- add novo item na carga
	void newItemDialog(){
		AlertDialog.Builder builderMsg = new AlertDialog.Builder(TryMapActivity.this);
		
		builderMsg.setTitle("Adicionado com Sucesso");
		builderMsg.setMessage("Deseja adicionar novas notas ficais ou finalizar a montagem?");
		builderMsg.setNegativeButton("Adicionar Destino", new  AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				screenMount();
			}
		});
		builderMsg.setPositiveButton("Finalizar", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				 screenConfirm();
			}
		});
		AlertDialog alertMsg = builderMsg.create();
		alertMsg.setTitle("Adicionado com Sucesso");
		alertMsg.show();
		
	}
	
	void backConfirmDialog(){
		AlertDialog.Builder builderMsg = new AlertDialog.Builder(TryMapActivity.this);

		builderMsg.setTitle("Cancelar");
		builderMsg.setMessage("Deseja adicionar novas notas fiscais ou cancelar a ação?");
		builderMsg.setPositiveButton("Cancelar", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				 screenMain();
			}
		});
		builderMsg.setNegativeButton("Adicionar", new  AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				screenMount();
			}
		});
		AlertDialog alertMsg = builderMsg.create();
		alertMsg.setTitle("Adicionado com Sucesso");
		alertMsg.show();
	}
	
	//-- add novo produto ah lista
	void newProdDialog(){
		AlertDialog.Builder builderMsg = new AlertDialog.Builder(TryMapActivity.this);
		
		builderMsg.setTitle("Adicionado com sucesso");
		builderMsg.setMessage("Continuar adicionando notas fiscais?");
		builderMsg.setPositiveButton("Sim", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				 screenProduct();
			}
		});
		builderMsg.setNegativeButton("Não", new  AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				screenMain();
			}
		});
		AlertDialog alertMsg = builderMsg.create();
		alertMsg.setTitle("Adicionado com Sucesso");
		alertMsg.show();
		
	}
	
	void showInfo(int opc){
		
		String info = "";
		String title = "";
		
		switch(opc){
			case 1:
				title = "Montagem de Roteiro";
				info =  "Serão escolhidas a cidade de origem, os destinos e qual a nota fiscal destinada a ela.\n\n" +
						"Ao clicar em adicionar poderá escolhar um novo destino ou finalizar a nota fiscal.\n\n" +
						"Cada destino pode ter apenas uma nota fiscal relacionada.";
				break;
			case 2:
				title = "Confirmação de Roteiro";
				info =  "Pode-se alterar a cidade de origem, cancelar ou confirmar a montagem de reoteiro. \n\n" +
						"Será gerada um roteiro e a ordem de entrega das notas fiscais.";
				break;	
			case 3:
				title = "Cadastro de Nota Fiscal";
				info = "Cadastro de novas notas fiscais que irão ser adicionados aos seus destinos";
				break;	
			case 4:
				title = "Rota";
				info = "Esta é a rota calculada contendo a ordem das cidades que deverão ser percorridas"+
						"e a ordem na qual as notas fiscais deverão ser entregues.\n\n" +
						"Para gerar uma nova rota clique no botão de retorno.";
				break;	
		}
		
		
		AlertDialog.Builder builderMsg = new AlertDialog.Builder(TryMapActivity.this);
		
		builderMsg.setTitle(title);
		builderMsg.setMessage(info);
		builderMsg.setPositiveButton("Ok", new  AlertDialog.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		});

		AlertDialog alertMsg = builderMsg.create();
		alertMsg.setTitle(title);
		alertMsg.show();
		
	}
	
	// -- ################################ ENVIRONMENT ################################
	//-- criacao de arcos e vertices
	void createEnv(){
		vertices = new ArrayList<Vertex>();
		arcos = new ArrayList<Edge>();
		
		for (int i = 0; i < 26; i++) {
			Vertex estado = new Vertex(stateArray[i] + "_" + i, stateArray[i]);
			vertices.add(estado);
		}
		
		addArco("Edge_0", 1, 15, 1283);	
		addArco("Edge_1", 1, 22, 806);	
		addArco("Edge_2", 1, 11, 330);	
		addArco("Edge_3", 2, 25, 524);	
		addArco("Edge_4", 2, 21, 1372);	
		addArco("Edge_5", 2, 9, 906);	
		addArco("Edge_6", 2, 4, 1453);
		addArco("Edge_7", 3, 1, 6083);			
		addArco("Edge_8", 4, 5, 694);			
		addArco("Edge_9", 5, 15, 1784);	
		addArco("Edge_10", 5, 1, 2941);	
		addArco("Edge_10", 5, 13, 2357);	
		addArco("Edge_11", 5, 17, 1456);
		addArco("Edge_12", 6, 23, 408);	
		addArco("Edge_13", 6, 4, 991);		
		addArco("Edge_14", 7, 6,300);			
		addArco("Edge_15", 8, 14, 537);	
		addArco("Edge_16", 8, 10, 688);	
		addArco("Edge_17", 8, 18, 800);	
		addArco("Edge_18", 9, 21, 1643);	
		addArco("Edge_19", 9, 15, 874);	
		addArco("Edge_20", 9, 5, 934);	
		addArco("Edge_21", 9, 4, 935);
		addArco("Edge_22", 12, 0, 294);			
		addArco("Edge_23", 13, 1, 5298);	
		addArco("Edge_24", 13, 3, 785);	
		addArco("Edge_25", 13, 17, 901);	
		addArco("Edge_26", 14, 10, 185);			
		addArco("Edge_27", 15, 21, 1454);	
		addArco("Edge_28", 15, 24, 1401);	
		addArco("Edge_29", 15, 22, 1386);	
		addArco("Edge_30", 16, 7, 476);						
		addArco("Edge_31", 18, 12, 285);	
		addArco("Edge_32", 18, 21, 839);	
		addArco("Edge_33", 18, 10, 120);	
		addArco("Edge_34", 19, 13, 1445);	
		addArco("Edge_35", 19, 17, 544);		
		addArco("Edge_36", 20, 2, 434);	
		addArco("Edge_37", 20, 25, 521);		
		addArco("Edge_38", 21, 0, 356);			
		addArco("Edge_39", 22, 24, 446);	
		addArco("Edge_40", 22, 21, 1599);		
		addArco("Edge_41", 23, 4, 1014);	
		addArco("Edge_42", 23, 20, 429);	
		addArco("Edge_43", 23, 2, 586);	
		addArco("Edge_44", 24, 8, 634);	
		addArco("Edge_45", 24, 18, 1137);	
		addArco("Edge_46", 24, 21, 1163);	
		addArco("Edge_47", 25, 21, 1202);			

		
		isEnvOk = true;
	}
	
	//-- criacao dos arcos
	void addArco(String id, int inicio, int fim, int peso) {
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
//        listProducts.add("Carga");
//        listProducts.add("Pacotes");
//        listProducts.add("Toneis");
//        listProducts.add("Caixotes");
//        listProducts.add("Containers");

	}
	
	public static Vertex getKeyFromValue(HashMap hm, Object value) {
	    for (Object o : hm.keySet()) {
	      if (hm.get(o).equals(value)) {
	    	  System.out.println((String) o.toString());
	        return (Vertex) o;
	      }
	    }
	    return null;
	  }
    
}