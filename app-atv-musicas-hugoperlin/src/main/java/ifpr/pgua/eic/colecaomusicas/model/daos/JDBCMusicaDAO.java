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
import ifpr.pgua.eic.colecaomusicas.utils.DBUtils;

public class JDBCMusicaDAO implements MusicaDAO{
    private static final String INSERTSQL = "INSERT INTO musicas(nome,duracao,anoLancamento,artistaId,generoId) VALUES (?,?,?,?,?)";
    private static final String SELECTSQL = "SELECT * FROM musicas";

    private FabricaConexoes fabrica;
    private ArtistaDAO artistaDAO;
    private GeneroDAO generoDAO;

    public JDBCMusicaDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Musica musica) {
        try (Connection con = fabrica.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(INSERTSQL, Statement.RETURN_GENERATED_KEYS);
            
            pstm.setString(1, musica.getNome());
            pstm.setInt(2, musica.getDuracao());
            pstm.setInt(3, musica.getAnoLancamento());
            pstm.setInt(4, musica.getArtista().getId());
            pstm.setInt(5, musica.getGenero().getId());

            int ret = pstm.executeUpdate();

            if(ret == 1){
                int id = DBUtils.getLastId(pstm);

                musica.setId(id);

                return Resultado.sucesso("Música cadastrada", musica);
            }
            return Resultado.erro("Erro desconhecido!");


        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado listar() {
        
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement(SELECTSQL);

            ResultSet rs = pstm.executeQuery();

            ArrayList<Musica> lista = new ArrayList<>();
            while(rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int duracao = rs.getInt("duracao");
                int anoLancamento = rs.getInt("anoLancamento");

                //iremos buscar artista e genero através do repositório
                Musica musica = new Musica(id,nome, anoLancamento, duracao, null, null);

                lista.add(musica);
            }

            return Resultado.sucesso("Musicas listadas", lista);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    
    }

    @Override
    public Resultado atualizar(int id, Musica nova) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }

    @Override
    public Resultado deletar(int id) {
        
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement("DELETE FROM musicas WHERE id = (?)");
            pstm.setInt(1, id);

            int rs = pstm.executeUpdate();

            if(rs > 0){
                return Resultado.sucesso("Musica apagada", id);
            }

            return Resultado.erro("Erro desconhecido!");

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }

    }

    @Override
    public Resultado buscarPorID(int id) {
        
        try (Connection con = fabrica.getConnection()) {
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM musicas WHERE id = (?)");

            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            ArrayList<Musica> lista = new ArrayList<>();
            while(rs.next()){
                int idM = rs.getInt("id");
                String nome = rs.getString("nome");
                int duracao = rs.getInt("duracao");
                int anoLancamento = rs.getInt("anoLancamento");
                Musica musica = new Musica(idM, nome, anoLancamento, duracao, null, null);
                lista.add(musica);
            }
            return Resultado.sucesso("Musica",lista);
        }catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }
}
