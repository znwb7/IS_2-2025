package com.ucv.model;
import java.io.*;

public class DataBase {
   
    private static void CrearArchivo() {
        new File("DataBase.txt");
    }

    public static void main(String[] args) {
    }

    public static void ComprobarDatos(String ID, String Password) {
        String ArchiveName = "DataBase.txt"; 
        try (BufferedReader br = new BufferedReader(new FileReader(ArchiveName))) {
            String Line;
            boolean DaDaCo1 = false, DaDaCo2 = false;
            while ((Line = br.readLine()) != null) {
                String[] Word = Line.split(" ");
                if (Word[1].equals(ID)){
                    DaDaCo1= true;                   
                }
                if ( Word[2].equals(Password)){
                    DaDaCo2= true;
                }
                if (DaDaCo1 || DaDaCo2) {
                    break;
                }
            }            
            if (DaDaCo1 == false || DaDaCo2 == false) {
                System.out.println("Usuario no encontrado en la base de datos.");
            }
            if (DaDaCo1 && DaDaCo2 == false){
                System.out.println("Contraseña Incorrecta");
            }
        } catch (IOException e) {}
    }

    public static void Registro(String Name, String ID, String Password, String RutaSecretaria) {
        CrearArchivo();
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("DataBase.txt", true))) {
            BufferedReader br = new BufferedReader(new FileReader("DataBase.txt"));
            String Line;           
            while ((Line = br.readLine()) != null) { //si ya existe en la database
                String[] Word = Line.split(" ");
                if (Word[1].equals(ID)){
                    System.out.println("ya existes en la database");
                    return;
                }
            }
            String Rol = FindUser(ID, RutaSecretaria);            
            if (Rol == null){
                System.out.println("no hay vida");
                return;
            }else{

                String NLine = Name + " " + ID + " " + Password + " " + Rol;
                escritor.write(NLine);
                escritor.newLine();
            }

        } 
        catch (IOException e) {}
    }

    private static String FindUser (String ID, String RutaSecretaria){
        try (BufferedReader br = new BufferedReader(new FileReader(RutaSecretaria))) {
            String Line;
            while ((Line = br.readLine()) != null) {
                String[] Word = Line.split(" ");
                if (Line.isEmpty()) continue;
                if (Word[1].equals(ID)) {
                    return Word[3]; 
                }
            } 
            return null; 
        } catch (IOException e) { 
            return null; 
        }
    }
}