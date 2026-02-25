package com.ucv.controller;

import com.ucv.model.ccb;

public class ccbController {

	public static float calcularCCB (float CF, float CV, float NB, float merma) throws Exception {
		try {
			// 1. Validación de números negativos
			if (CF <= 0 || CV <= 0 || NB <= 0 || merma <= 0) {
				throw new Exception("NUM_NEGATIVO");
			}

			// 2. Ejecución de la lógica del modelo
			ccb.EcuacionesCCB(CF, CV, NB, merma);
			return ccb.getCCB();

		} catch (NumberFormatException e) {
			// Este captura si intentan meter letras (si el parseo ocurre aquí dentro)
			throw new Exception("CARACTER_INVALIDO");
		} catch (Exception e) {
			// Este captura tu excepción de números negativos o cualquier otro error
			throw e; 
		}
	}
}
