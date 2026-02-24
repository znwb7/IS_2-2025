package com.ucv.controller;

import com.ucv.model.ccb;

public class ccbController {

	public static float calcularCCB(float CF, float CV, float NB, float merma) {
		ccb.EcuacionesCCB(CF, CV, NB, merma);
		return ccb.getCCB();
	}
}
