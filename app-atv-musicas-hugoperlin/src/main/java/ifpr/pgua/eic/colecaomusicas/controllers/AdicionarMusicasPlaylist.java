package ifpr.pgua.eic.colecaomusicas.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.colecaomusicas.App;
import ifpr.pgua.eic.colecaomusicas.model.entities.Musica;
import ifpr.pgua.eic.colecaomusicas.model.entities.Playlist;
import ifpr.pgua.eic.colecaomusicas.model.repositories.RepositorioMusicas;
import ifpr.pgua.eic.colecaomusicas.model.repositories.RepositorioPlaylist;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

public class AdicionarMusicasPlaylist implements Initializable{

    @FXML
    private TextField tfNome;

    @FXML
    private ListView<Musica> lstMusicas;

    @FXML
    private ComboBox<Playlist> cbPlaylist;

    private RepositorioPlaylist repositorioPlaylist;
    private RepositorioMusicas repositorioMusicas;

    public AdicionarMusicasPlaylist(RepositorioPlaylist repositorio, RepositorioMusicas repositorio2){
        this.repositorioPlaylist = repositorio;
        this.repositorioMusicas = repositorio2;
    }

    @FXML
    void voltar(ActionEvent event) {
        App.popScreen();
    }


    @FXML
    void criarPlaylist(ActionEvent event) {
        String nome = tfNome.getText();

        String msg = repositorioPlaylist.cadastrarPlaylist(nome);

        Resultado r = repositorioPlaylist.listar();
        List<Playlist> lista = (List)r.comoSucesso().getObj();
        cbPlaylist.getItems().clear();
        cbPlaylist.getItems().addAll(lista);

        Alert alert = new Alert(AlertType.INFORMATION,msg);
        alert.showAndWait();
    }

    @FXML
    void cadastrar(ActionEvent event){
        int idP = cbPlaylist.getValue().getId();
        Musica musicaID = lstMusicas.getSelectionModel().getSelectedItem();

        List<Musica> selecionados = lstMusicas.getSelectionModel().getSelectedItems();

        ArrayList<Integer> idMusicas = new ArrayList<>();
        for(Musica musica:selecionados){
            idMusicas.add(musica.getId());
        }

        if(musicaID != null && idP != 0){

            for (Integer integer : idMusicas) {
                repositorioPlaylist.adicionarMusica(idP, integer);
            }
            String str = "MUSICAS ADICIONADAS A PLAYLIST!";
            Alert alert = new Alert(AlertType.INFORMATION, str);
            alert.showAndWait();
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        App.popScreen();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){            
        Resultado r = repositorioPlaylist.listar();

        if(r.foiSucesso()){
            List<Playlist> lista = (List)r.comoSucesso().getObj();
            cbPlaylist.getItems().addAll(lista);
        }else{
            Alert alert = new Alert(AlertType.ERROR, r.getMsg());
            alert.showAndWait();
        }

        Resultado r1 = repositorioPlaylist.listar();
        
         
        lstMusicas.getItems().clear();
        
        lstMusicas.getSelectionModel()
              .setSelectionMode(SelectionMode.MULTIPLE);
        
        
        Resultado resultado = repositorioMusicas.listar();

        if(resultado.foiSucesso()){
            List<Musica> lista = (List)resultado.comoSucesso().getObj();
            lstMusicas.getItems().addAll(lista);
        }else{
            Alert alert = new Alert(AlertType.ERROR, resultado.getMsg());
            alert.showAndWait();
        }
    }
}
