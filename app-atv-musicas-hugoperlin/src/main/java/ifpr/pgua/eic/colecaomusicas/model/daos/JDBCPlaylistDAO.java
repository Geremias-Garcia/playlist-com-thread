package ifpr.pgua.eic.colecaomusicas.model.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.colecaomusicas.model.entities.Artista;
import ifpr.pgua.eic.colecaomusicas.model.entities.Genero;
import ifpr.pgua.eic.colecaomusicas.model.entities.Musica;
import ifpr.pgua.eic.colecaomusicas.model.entities.Playlist;
import ifpr.pgua.eic.colecaomusicas.model.repositories.RepositorioMusicas;

public class JDBCPlaylistDAO implements PlaylistDAO{
    private static final String INSERTSQL = "INSERT INTO playlists(nome) VALUES (?)";
    private static final String SELECTSQL = "SELECT * FROM musicas";

    private FabricaConexoes fabrica;
    private Playlist playlist;
    private MusicaDAO musicaDAO;

    public JDBCPlaylistDAO(FabricaConexoes fabrica){
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Playlist nome) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(INSERTSQL, Statement.RETURN_GENERATED_KEYS);
            
            // Ajustar os parâmetros
            pstm.setString(1, nome.getNome());
            // Executar o comando
            int ret = pstm.executeUpdate();

            con.close();

            if (ret == 1) {
                return Resultado.sucesso("Playlist cadastrada", playlist);
            }
            return Resultado.erro("Erro não identificado!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado listar() {
        try {
            Connection con = fabrica.getConnection();

            PreparedStatement pstm = con.prepareStatement("SELECT * FROM playlists");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Playlist> lista = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                Playlist playlist = new Playlist(id, nome);
                lista.add(playlist);
            }
            rs.close();
            pstm.close();
            con.close();

            return Resultado.sucesso("Playlists existentes!", lista);
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado atualizar(int id, Playlist nome) {
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");

    }

    @Override
    public Resultado getById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Resultado adicionarMusica(int playlistId, int musicaId) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement("INSERT INTO musicas_playlist(id_playlist,id_musica) VALUES (?,?)");
            
            // Ajustar os parâmetros
            pstm.setInt(1, playlistId);
            pstm.setInt(2, musicaId);
            // Executar o comando
            int ret = pstm.executeUpdate();

            con.close();

            if (ret == 1) {
                return Resultado.sucesso("Musica cadastrada", playlist);
            }
            return Resultado.erro("Erro não identificado!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
    
    @Override
    public Resultado delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Resultado listarMusicasDaPlaylist(int id) {
        // TODO Auto-generated method stub

        try (Connection con = fabrica.getConnection()){

            PreparedStatement pstm = con.prepareStatement("SELECT * FROM musicas_playlist WHERE id_playlist = (?)");
            
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();
            
            ArrayList<Integer> musicas = new ArrayList<>();
            while(rs.next()){
                int idM = rs.getInt("id_musica");
                musicas.add(idM);
            }
            
            return Resultado.sucesso("Musica cadastrada", musicas);
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
    
}
