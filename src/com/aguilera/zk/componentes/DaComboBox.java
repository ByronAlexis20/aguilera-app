package com.aguilera.zk.componentes;

import java.util.Collection;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

/**
 * Variante personalizada del combobox de ZK.
 * Esta variante soporta:
 * 
 * withEmptyOption: Si es verdadero adiciona un elemento al inicio de la lista de opciones con el valor en nulo (vacío).
 * 
 */

public class DaComboBox extends Combobox{

	private static final long serialVersionUID = 1L;

    private boolean withEmptyOption = false;

    /**
     * Si es verdadero adiciona un elemento vacío al inicio de la lista de opciones. 
     * Al seleccionar el elemento se retorna null como item seleccionado.
     * 
     * @param pWithEmptyOption
     */
    public void setWithEmptyOption(final boolean pWithEmptyOption) {
        withEmptyOption = pWithEmptyOption;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setModel(final ListModel<?> pModel) {

        // If one wants to add an empty value
        if (withEmptyOption) {
            // we recreate the list
            ListModelList list = new ListModelList(pModel.getSize() + 1);
            // And add an empty value at the start of the list
            list.add(0, null);
            // Then the original list value
            list.addAll((Collection) pModel);
            super.setModel(list);
        } else {
            // Otherwise we do nothing special
            super.setModel(pModel);
        }
    }
    
}
