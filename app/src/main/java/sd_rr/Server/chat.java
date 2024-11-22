package sd_rr.Server;
import sd_rr.*;
import java.util.List;

public class chat {
    //id, nome, descricao, permissao
    private int id;
    private String nome;
    private String descricao;
    private PermissaoTypeEnum permissao;
    private List<User> users;

    public chat(int id, String nome, String descricao, PermissaoTypeEnum permissao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.permissao = permissao;
    }

    //adduser
    public void addUser(User user) {
        users.add(user);
    }

    //removeuser
    public void removeUser(User user) {
        users.remove(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public PermissaoTypeEnum getPermissao() {
        return permissao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPermissao(PermissaoTypeEnum permissao) {
        this.permissao = permissao;
    }

}
