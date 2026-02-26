package com.ucv.controller;

import com.ucv.model.PagoModel;
import com.ucv.model.PagoModel.ResultadoValidacion;
import com.ucv.view.BilleteraUCV;
import com.ucv.model.DataBase;

import javax.swing.*;
import java.time.LocalDate;

public class PagoController {

    private final PagoModel modelo;
    private final BilleteraUCV parentFrame;
    private ResultadoValidacion ultimoResultado;

    public PagoController(PagoModel modelo, BilleteraUCV parentFrame) {
        this.modelo = modelo;
        this.parentFrame = parentFrame;
    }

    public ResultadoValidacion getUltimoResultado() {
        return ultimoResultado;
    }

    /**
     * Valida el pago usando el modelo y actualiza la BD principal.
     * Solo muestra un mensaje de éxito en caso de que la recarga sea correcta.
     */
    public void procesarPago(String cedula, String monto, String referencia, JFrame parent) {
        ultimoResultado = modelo.validarYProcesarPago(cedula, monto, referencia);

        switch (ultimoResultado) {
            case CAMPOS_INVALIDOS:
                JOptionPane.showMessageDialog(parent,
                        "Por favor, complete todos los campos correctamente.",
                        "Error de Validación",
                        JOptionPane.WARNING_MESSAGE);
                break;

            case PAGO_NO_ENCONTRADO:
                JOptionPane.showMessageDialog(parent,
                        "Pago no encontrado. Verifique la cédula o la referencia.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case PAGO_YA_UTILIZADO:
                JOptionPane.showMessageDialog(parent,
                        "El pago ya fue utilizado previamente.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                break;

            case MONTO_INCORRECTO:
                JOptionPane.showMessageDialog(parent,
                        "El monto ingresado no coincide con el registrado.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case RECARGA_EXITOSA:
                // --- ACTUALIZAR SALDO EN BD PRINCIPAL ---
                try {
                    DataBase db = new DataBase();
                    DataBase.UpdateMoney resultado = db.UpdateMoney(cedula, LocalDate.now().toString());

                    if (resultado == DataBase.UpdateMoney.SALDO_ACTUALIZADO_CON_EXITO) {
                        // Solo mostrar mensaje en la vista del pago móvil
                        if (parent instanceof com.ucv.view.PagoMovilUCV) {
                            ((com.ucv.view.PagoMovilUCV) parent).getParentFrame().recargaExitosa();
                        }
                    } else {
                        JOptionPane.showMessageDialog(parent,
                                "Ocurrió un error al actualizar el saldo en la billetera.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(parent,
                            "Ocurrió un error inesperado al actualizar el saldo.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                break;

            case ERROR_SISTEMA:
            default:
                JOptionPane.showMessageDialog(parent,
                        "Ocurrió un error en el sistema. Intente más tarde.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
}