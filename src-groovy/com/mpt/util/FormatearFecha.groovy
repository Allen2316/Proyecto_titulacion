package com.mpt.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger

import com.mpt.constantes.MCEConstants;

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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MCEConstants.DATE_PATTERN, new Locale(MCEConstants.LANGUAGE_CODE, MCEConstants.COUNTRY_CODE));
		return LocalDate.now().format(formatter);
	}

	/**
	 * Permite obtener la fecha actual formateada (Día, mes y año).
	 *
	 * @param pattern, the pattern
	 * @return Fecha formateada en formato String
	 */
	static String obtenerFechaActualFormateada(String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, new Locale(MCEConstants.LANGUAGE_CODE, MCEConstants.COUNTRY_CODE));
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

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MCEConstants.DATE_PATTERN, new Locale(MCEConstants.LANGUAGE_CODE, MCEConstants.COUNTRY_CODE));
		return date.format(formatter);
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
}