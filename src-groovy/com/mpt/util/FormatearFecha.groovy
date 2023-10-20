package com.mpt.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger
import java.time.ZoneId;

import com.mpt.constantes.MPTConstants;

/**
 * Formatea la fecha actual o la fecha enviada enviada como parámetro
 * 
 * @author ajcm
 * @version 1.0
 */
class FormatearFecha {

	private static final Logger logger = Logger.getLogger(FormatearFecha.class.toString())

	/**
	 * Permite obtener la fecha actual formateada (Día, mes y año).
	 * 
	 * @return Fecha formateada en formato String
	 */
	static String obtenerFechaActualFormateada() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MPTConstants.DATE_PATTERN, new Locale(MPTConstants.LANGUAGE_CODE, MPTConstants.COUNTRY_CODE));
		return LocalDate.now().format(formatter);
	}

	/**
	 * Permite obtener la fecha actual formateada (Día, mes y año).
	 *
	 * @param pattern, the pattern
	 * @return Fecha formateada en formato String
	 */
	static String obtenerFechaActualFormateada(String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, new Locale(MPTConstants.LANGUAGE_CODE, MPTConstants.COUNTRY_CODE));
		return LocalDate.now().format(formatter);
	}

	/**
	 * Permite obtener la fecha y hora actual (Día, mes, año, hora, minutos, segundo).
	 *
	 * @return Fecha y hora en formato String
	 */
	static LocalDateTime obtenerFechaYHoraActual() {
		return LocalDateTime.now();
	}

	/**
	 * Formatea la fecha recibida.
	 * 
	 * @param date, the date
	 * @return Fecha formateada en formato String
	 */
	static String formatearFecha(LocalDate date) {
		if (!date) {
			throw new Exception("Se debe enviar una fecha")
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MPTConstants.DATE_PATTERN, new Locale(MPTConstants.LANGUAGE_CODE, MPTConstants.COUNTRY_CODE));
		return date.format(formatter);
	}
	
	/**
	 * Formatea la fecha String recibida.
	 * @param fechaString ejem, "2023-07-14"
	 * @return La fecha formateada en formato String
	 */
	static String formatearFecha(String fechaString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
		LocalDate fecha = LocalDate.parse(fechaString, formatter)
		
		DateTimeFormatter nuevoFormatter = DateTimeFormatter.ofPattern(MPTConstants.DATE_PATTERN, new Locale(MPTConstants.LANGUAGE_CODE, MPTConstants.COUNTRY_CODE));			   
		
		return fecha.format(nuevoFormatter)
	}

	/**
	 * Convierte una cadena que contiene la cantidad de horas a milisegundos. Es utilizado
	 * para el temporizador de los recordatorios
	 * 
	 * @param strHours
	 * @return
	 */
	static Long convertStringHoursToMilliseconds(String strHours) {
		if (strHours.isEmpty()) {
			throw new Exception("Se debe enviar strHours")
		}

		try {
			int hours = Integer.parseInt(strHours)
			Long ms = hours * 60 * 60 * 1000

			return ms
		} catch (NumberFormatException nfe) {
			logger.severe("No se pudo convertir la cadena:" + strHours + " a entero")
		}
	}

	/**
	 * Convierte las horas en una cadena con días y/o horas según sea el caso. Es utilizado 
	 * en el mensaje enviado en los recordatorios
	 * 
	 * @param strHours
	 * @return
	 */
	static String convertHoursToDays(String strHours) {
		if (strHours.isEmpty()) {
			throw new Exception("Se debe enviar strHours")
		}

		String resultTime = ""
		int oneDay = 24

		try {
			int hours = Integer.parseInt(strHours)
			if (hours < oneDay) {
				return hours + " hora(s)"
			}

			Double days = hours / oneDay
			Double decimalPart = days % 1
			int wholePart = days - decimalPart
			int hoursAux = Math.round(oneDay * decimalPart)

			resultTime = hoursAux > 0 ? (wholePart + " día(s) " + hoursAux + " hora(s)") : (wholePart + " día(s)")
			
		} catch (Exception e) {
			logger.severe("No se pudo convertir la cadena:" + strHours + " a días")
		}

		return resultTime
	}
	
	
	/**
	 * Calcula la fecha de los 8 días antes de la fecha proporcionada.
	 *
	 * @param fecha La fecha a partir de la cual se calculará los 8 días antes.
	 * @return La fecha de los 8 días antes de la fecha proporcionada.
	 */
	static Date fechaRecordatorio(LocalDate fecha) {
		LocalDate fechaOchoDiasAntes = fecha.minusDays(8);
		return Date.from(fechaOchoDiasAntes.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}	
	
	
	/**
	 * Comprueba si una tarea puede realizarse con respecto a su fecha de finalización.
	 * La tarea puede realizarse si la fecha de finalización no es anterior a la fecha actual.
	 *
	 * @param fechaFinalizacion La fecha de finalización de la tarea en formato LocalDate.
	 * @return {@code true} si la tarea puede realizarse, {@code false} si la fecha de finalización ya ha pasado.
	 */
	public static boolean fechaCulminacionDisponible(LocalDate fechaFinalizacion) {
        LocalDate fechaActual = LocalDate.now();
        return !fechaFinalizacion.isBefore(fechaActual);
    }
	
	/**
	 * Obtiene la fecha y hora actual en formato clave-valor.
	 *
	 * @return Un mapa con los campos "dia", "mes", "anio" y "hora" como claves y sus respectivos valores.
	 */
	public static Map<String, String> obtenerFechaYHoraSeparada() {
        Map<String, String> fechaYHoraActual = new HashMap<>();
        LocalDateTime fechaHoraActual = LocalDateTime.now();
                             
    
        String dia = obtenerDiaLetras(fechaHoraActual);
        String mes = obtenerMesLetras(fechaHoraActual);
        String anio = obtenerAnioLetras(fechaHoraActual);
        String hora = obtenerHoraLetras(fechaHoraActual);
        
        fechaYHoraActual.put("dia", dia);
        fechaYHoraActual.put("mes", mes);
        fechaYHoraActual.put("anio", anio);
        fechaYHoraActual.put("hora", hora);
    
        return fechaYHoraActual;
    }

    private static String obtenerDiaLetras(LocalDateTime fechaHora) {
        int dia = fechaHora.getDayOfMonth();
        return convertirNumeroALetras(dia);
    }

    private static String obtenerMesLetras(LocalDateTime fechaHora) {
        DateTimeFormatter formatoMes = DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES"));
        return fechaHora.format(formatoMes);
    }

    private static String obtenerAnioLetras(LocalDateTime fechaHora) {
        int anio = fechaHora.getYear();
        return convertirNumeroALetras(anio);
    }

    private static String obtenerHoraLetras(LocalDateTime fechaHora) {
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH'h'mm");
        return fechaHora.format(formatoHora);
    }

	private static String convertirNumeroALetras(int numero) {
	    if (numero < 1 || numero > 9999) {
	        return "Número fuera del rango admitido.";
	    }

	    String[] unidades = ["", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE", "DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISÉIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"];
	    String[] decenas = ["", "", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"];
	    String[] centenas = ["", "CIEN", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"];
	
	    if (numero < 20) {
	        return unidades[numero];
	    } else if (numero < 100) {
	        int unidad = numero % 10;
	        int decena = numero / 10;
	        return decenas[decena] + (unidad != 0 ? (" Y " + unidades[unidad]) : "");
	    } else if (numero < 1000) {
	        int unidad = numero % 10;
	        int decena = (numero % 100) / 10;
	        int centena = numero / 100;
	        return centenas[centena] + (decena != 0 ? (" " + decenas[decena]) : "") + (unidad != 0 ? (" Y " + unidades[unidad]) : "");
	    } else {
	        int unidad = numero % 10;
	        int decena = (numero % 100) / 10;
	        int centena = (numero % 1000) / 100;
	        int millar = numero / 1000;
	        return unidades[millar] + " MIL" + (centena != 0 ? (" " + centenas[centena]) : "") + (decena != 0 ? (" " + decenas[decena]) : "") + (unidad != 0 ? (" " + unidades[unidad]) : "");
	    }
    }
	
}