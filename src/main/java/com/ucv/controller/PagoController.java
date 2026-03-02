package com.ucv.controller;

import com.ucv.model.PagoModel;
import com.ucv.model.PagoModel.ResultadoValidacion;
import com.ucv.model.DataBase;

public class PagoController {

    private final PagoModel modelo;
    private final String usuarioID;

    public PagoController(PagoModel modelo, String usuarioID) {
        this.modelo = modelo;
        this.usuarioID = usuarioID;
    }

    public ResultadoValidacion procesarPago(String cedula, String monto, String referencia) {

        ResultadoValidacion resultado = modelo.validarYProcesarPago(cedula, monto, referencia);

        if (resultado == ResultadoValidacion.RECARGA_EXITOSA) {
            try {
                DataBase db = new DataBase();

                DataBase.EnumUpdateMoney update =
                        db.UpdateMoney(usuarioID, monto);

                if (update != DataBase.EnumUpdateMoney.SALDO_ACTUALIZADO_CON_EXITO) {
                    return ResultadoValidacion.ERROR_SISTEMA;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResultadoValidacion.ERROR_SISTEMA;
            }
        }

        return resultado;
    }

    public double obtenerSaldo() {
        try {
            DataBase db = new DataBase();
            return db.obtenerSaldo(usuarioID);
        } catch (Exception e) {
            return 0;
        }
    }
}