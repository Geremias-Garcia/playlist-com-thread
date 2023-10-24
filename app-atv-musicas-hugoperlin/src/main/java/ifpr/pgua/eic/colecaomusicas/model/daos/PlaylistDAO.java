package ifpr.pgua.eic.colecaomusicas.model.daos;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.colecaomusicas.model.entities.Genero;
import ifpr.pgua.eic.colecaomusicas.model.entities.Playlist;

public interface PlaylistDAO {
    Resultado criar(Playlist nome);

    //r
    Resultado listar();
    Resultado getById(int id);
    Resultado adicionarMusica(int playlistId, int musicaId);
    Resultado listarMusicasDaPlaylist(int id);
    //u
    Resultado atualizar(int id, Playlist novo);

    //d
    Resultado delete(int id);
}
