package com.aguilera.util;

import java.util.Comparator;

import com.aguilera.modelo.Opcion;

public class SortOpcionByOrden implements Comparator<Opcion>{

	@Override
	public int compare(Opcion o1, Opcion o2) {
		// TODO Auto-generated method stub
		return o1.getOrden() - o2.getOrden();
	}
}
