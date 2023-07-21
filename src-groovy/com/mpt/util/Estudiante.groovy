package com.mpt.util

import java.util.logging.Logger

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.identity.UserNotFoundException

import com.mpt.constantes.MPTConstants

/**
 * Obtiene ciertos datos de un estudiante
 * 
 * @author ajcm
 * @version 1.0
 */
class Estudiante {
	
	private static final Logger logger = Logger.getLogger(Estudiante.class.toString())

	/**
	 * Obtiene los nombres completos del estudiante solicitante (El cual inició el proceso) utilizando el idSolicitanteBonitaBPM (Es el userId).
	 * ObtenerEstudiante.obtenerNombre(apiAccessor.getIdentityAPI(), solicitud.idSolicitanteBonitaBPM)
	 * 
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM
	 * @return String
	 */
	static String getFullName(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM) {
		if (!idSolicitanteBonitaBPM) {
			throw new Exception("Se debe enviar el idSolicitanteBonitaBPM del estudiante solicitante getFullName")
		}

		String fullNameStudent = ""
		try{
			fullNameStudent = identityAPI.getUser(idSolicitanteBonitaBPM).firstName +
					" " + identityAPI.getUser(idSolicitanteBonitaBPM).lastName;
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización getFullName: " + e.getMessage())
		}

		return fullNameStudent.trim()
	}
			
	

	/**
	 * Obtiene la cédula del estudiante solicitante
	 * 
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM
	 * @return String
	 */
	static String getCedulaStudent(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM) {
		if (!idSolicitanteBonitaBPM) {
			throw new Exception("Se debe enviar el idSolicitanteBonitaBPM del estudiante solicitante getCedulaStudent")
		}

		String cedulaEstudiante = ""
		try{
			cedulaEstudiante = identityAPI.getCustomUserInfo(idSolicitanteBonitaBPM, 0, 1)
					.find { MPTConstants.IDENTIFICATION_DOCUMENT_TYPE == it.getDefinition().getName() }
					?.getValue();
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización: getCedulaStudent " + e.getMessage())
		}

		return cedulaEstudiante.trim()
	}
	/**
	 * Obtiene el ID del estudiante que inició el proceso, ideal cuando no se tiene acceso a la variable de negocio solicitud
	 * @param apiAccessor 
	 * @param userId
	 * @return El ID del usuario
	 */
	static Long getId(APIAccessor apiAccessor, Long userId) {
		Long userIdS = -1L;
		try{
			 userIdS = apiAccessor.getIdentityAPI().getUser(userId).getId()							
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado: getId " + e.getMessage())
		}
		return userIdS;
	}
}