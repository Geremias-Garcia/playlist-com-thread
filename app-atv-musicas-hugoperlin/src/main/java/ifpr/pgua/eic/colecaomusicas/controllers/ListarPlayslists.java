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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;

public class ListarPlayslists implements Initializable{

    
    @FXML
    private ListView<Playlist> lstPlaylist;

    @FXML
    private ListView<Musica>
    lstMusica;

    @FXML
    private ProgressIndicator piIndicator;
 
    private RepositorioPlaylist repositorioPlaylist;
    private RepositorioMusicas repositorioMusicas;

    public ListarPlayslists(RepositorioPlaylist playlist, RepositorioMusicas musicas){
        this.repositorioPlaylist = playlist;
        this.repositorioMusicas = musicas;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Thread t = new Thread(criarTarefa());

        t.start();
    }

    @FXML
    private void mostrarDetalhes(MouseEvent evento){
        lstMusica.getItems().clear();
        Playlist playlist = lstPlaylist.getSelectionModel().getSelectedItem();
        System.out.println(playlist.getId());

        Resultado r = repositorioPlaylist.listarMusicasDaPlaylist(playlist.getId());
        
        //Musica musicaAcesso;
        List<Musica> listMusic;
        if(r.foiSucesso()){
            List<Integer> list = (List)r.comoSucesso().getObj(); 
            for (Integer musica : list) {
                Resultado r1 = repositorioMusicas.buscarPorID(musica);
                listMusic = (List)r1.comoSucesso().getObj();
                lstMusica.getItems().addAll(listMusic);
            }
        }
    }

    @FXML
    void voltar(ActionEvent event) {
        App.popScreen();
    }

    private Runnable criarTarefa() {
        return new Runnable() {
            public void run() {

                Resultado r = repositorioPlaylist.listar();

                Platform.runLater(()->{
                    piIndicator.setVisible(false);
                    if(r.foiSucesso()){
                        List<Playlist> lista = (List)r.comoSucesso().getObj();
                        lstPlaylist.getItems().addAll(lista);
                    }else{
                        Alert alert = new Alert(AlertType.ERROR, r.getMsg());
                        alert.showAndWait();
                    }
                });

            }
        };
    }
    
}
