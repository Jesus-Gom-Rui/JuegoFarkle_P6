package org.UABC.farkle;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculadoraDePuntos {
    public int evaluarTiro(ArrayList<Dado> dadosSeleccionados){
        int totalPuntos=0;

        //Se hará un mapeo donde la clave será la cara del dado y el valor es cuantas veces salió.
        Map<Integer,Long> frecuencias=dadosSeleccionados.stream().collect(Collectors.groupingBy(Dado::getvalorVisible, Collectors.counting()));

        //Se valida la escalera.
        if(frecuencias.size()==6){
            return PuntajesFarkle.ESCALERA.getValor();
        }

        //Se verifica si hay exactemente 3 caras diferentes y si todas tienen exactamente dos dados.
        boolean tresPares=frecuencias.size()==3 && frecuencias.values().stream().allMatch(cantidad-> cantidad==2);
        if(tresPares){
            return PuntajesFarkle.TRES_PARES.getValor();
        }

        //Se harán las evaluaciones de los multiples, tríos y dados individuales.
        for(Map.Entry<Integer, Long> entry: frecuencias.entrySet()){
            int cara= entry.getKey();
            long cantidad= entry.getValue();
            if(cantidad==6){
                totalPuntos+=PuntajesFarkle.SEIS_IGUALES.getValor();
            }
            else if(cantidad==5){
                totalPuntos+=PuntajesFarkle.CINCO_IGUALES.getValor();
            }else if(cantidad==4){
                totalPuntos+=PuntajesFarkle.CUATRO_IGUALES.getValor();
            }else if(cantidad==3){
                switch(cara){
                    case 1: totalPuntos+=PuntajesFarkle.TRIO_UNOS.getValor();
                    break;
                    case 2: totalPuntos+=PuntajesFarkle.TRIO_DOS.getValor();
                    break;
                    case 3: totalPuntos+=PuntajesFarkle.TRIO_TRES.getValor();
                    break;
                    case 4: totalPuntos+=PuntajesFarkle.TRIO_CUATRO.getValor();
                    break;
                    case 5: totalPuntos+=PuntajesFarkle.TRIO_CINCO.getValor();
                    break;
                    case 6: totalPuntos+=PuntajesFarkle.TRIO_SEIS.getValor();
                    break;
                }

            }else if(cantidad<3){
                if(cara==1){
                    totalPuntos+=(int) cantidad*PuntajesFarkle.UNO.getValor();
                } else if(cara==5){
                    totalPuntos+=(int) cantidad*PuntajesFarkle.CINCO.getValor();
                }
            }

        }
        return totalPuntos;
    }

}
