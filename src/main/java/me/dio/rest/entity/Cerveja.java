package me.dio.rest.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cerveja")
public class Cerveja implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 200)
    @Column(nullable = false, unique = true)
    private String nome;
    
    @NotNull
    @Size(min = 1, max = 200)
    @Column(nullable = false)
    private String marca;
    
    @NotNull
    @Max(500)
    @Column(nullable = false)
    private Integer maximo;
    
    @NotNull
    @Max(100)
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCerveja tipo;

    public Cerveja() {    }

    public Cerveja(Long id, String nome, String marca, Integer maximo, Integer quantidade, TipoCerveja tipo) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.maximo = maximo;
        this.quantidade = quantidade;
        this.tipo = tipo;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public TipoCerveja getTipo() {
        return tipo;
    }

    public void setTipo(TipoCerveja tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Cerveja{" + "id=" + id + ", nome=" + nome + ", marca=" + marca + ", maximo=" + maximo + ", quantidade=" + quantidade + ", tipo=" + tipo + '}';
    }

    @Override
    public int hashCode() {
        Integer hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cerveja other = (Cerveja) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

}
