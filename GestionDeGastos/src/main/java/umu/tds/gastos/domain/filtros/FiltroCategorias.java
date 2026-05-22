package umu.tds.gastos.domain.filtros;

import java.util.List;
import umu.tds.gastos.domain.core.Gasto;
import umu.tds.gastos.domain.core.Categoria;

public class FiltroCategorias implements Filtro {
    private final List<Categoria> categorias;

    public FiltroCategorias(List<Categoria> categorias) {
        this.categorias = new java.util.ArrayList<>(categorias);
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    @Override
    public boolean cumple(Gasto g) {
        if (g == null || g.getCategoria() == null) {
            return false;
        }
        if(categorias == null || categorias.isEmpty()) { 
            return true;
        }   
        return categorias.contains(g.getCategoria());
    }
}
