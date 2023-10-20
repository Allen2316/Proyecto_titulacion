package com.mpt.payloads

import java.util.logging.Logger

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.identity.User

import com.mpt.constantes.CertificateFields
import com.mpt.constantes.MPTConstants

import groovy.json.JsonBuilder

/**
 * Permite obtener y construir el payload a enviar al servicio web de firma digital.
 * 
 * @author Allen
 * @version 1.0
 */
class Payload {

	private static final Logger logger = Logger.getLogger(Payload.class.toString())

	
	/**
	 * Permite obtener la cédula del estudiante responsable de firmar el documento.
	 *
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM
	 * @return String
	 */
	private static String getCedula(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM) {
		if (!idSolicitanteBonitaBPM) {
			throw new Exception("Se debe enviar el idSolicitanteBonitaBPM del estudiante solicitante que va a firmar")
		}

		String cedulaEstudiante = identityAPI.getCustomUserInfo(idSolicitanteBonitaBPM, 0, 1)
				.find { MPTConstants.IDENTIFICATION_DOCUMENT_TYPE == it.getDefinition().getName() }
				?.getValue();

		if (cedulaEstudiante.isEmpty()) {
			logger.severe("--> El estudiante no tiene cédula!")
		}

		return cedulaEstudiante.trim()
	}

	/**
	 * Permite generar el payload a enviar al servicio web de firma
	 * 
	 * @param groupName indica a que grupo pertenece el usuario que requiere el payload
	 * @param pdfGenerado
	 * @param pdfFirmado
	 * @param processAPI
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM para obtener cedula del firmante
	 * @param processDefinitionId
	 * @return String en formato json
	 */
	static String buildPayload(String groupName, Document pdfGenerado, Document pdfFirmado, ProcessAPI processAPI, IdentityAPI identityAPI,
			Long idSolicitanteBonitaBPM, Long processDefinitionId) {
		if (groupName.isEmpty()) {
			throw new Exception("Se debe enviar el groupName")
		}

		def jsonBuilder = new JsonBuilder()
		String futurePDFName = ""
		String pdfEncoded = ""
		String cedula = ""

		try {
			if (groupName.equals(CertificateFields.STUDENT_GROUP_NAME)) {
				// Si el estudiante va a firmar siempre se utiliza el pdfGenerado
				futurePDFName = getFutureFilenamePDF(pdfGenerado.getContentFileName());
				pdfEncoded = generarBase64(processAPI, pdfGenerado)
				cedula = getCedula(identityAPI, idSolicitanteBonitaBPM)
			}

			if (groupName.equals(CertificateFields.SECRETARY_GROUP_NAME)) {
				// Si la secretaria va a firmar siempre se utiliza el pdfGenerado
				futurePDFName = getFutureFilenamePDF(pdfGenerado.getContentFileName());
				pdfEncoded = generarBase64(processAPI, pdfGenerado)
				cedula = getCedula(identityAPI, idSolicitanteBonitaBPM)
			}

			if (groupName.equals(CertificateFields.COORDINATION_GROUP_NAME)) {
				if (pdfFirmado == null) {
					futurePDFName = getFutureFilenamePDF(pdfGenerado.getContentFileName());
					pdfEncoded = generarBase64(processAPI, pdfGenerado)
				} else {
					futurePDFName = getFutureFilenamePDF(pdfFirmado.getContentFileName());
					pdfEncoded = generarBase64(processAPI, pdfFirmado)
				}

				cedula = getCedula(identityAPI, idSolicitanteBonitaBPM)
			}

			if (groupName.equals(CertificateFields.DECAN_GROUP_NAME)) {
				// El decano siempre recibe un pdfFirmado
				futurePDFName = getFutureFilenamePDF(pdfFirmado.getContentFileName());
				pdfEncoded = generarBase64(processAPI, pdfFirmado)
				cedula = getCedula(identityAPI, idSolicitanteBonitaBPM)
			}

			jsonBuilder(cedula: "${cedula}",
			sistema: MPTConstants.SYSTEM_NAME,
			documentos: [[nombre: "${futurePDFName}", documento: "${pdfEncoded}"]])
			
			logger.info("-----> PYALOAD "+jsonBuilder.toString())
			return jsonBuilder.toString()

		} catch (Exception e) {
			throw new Exception("Error: " + e)
		}
	}
	
	/**
	 * Permite generar el payload de la lista de documentos a enviar al servicio web de firma
	 *
	 * @param groupName indica a que grupo pertenece el usuario que requiere el payload
	 * @param pdfGenerado
	 * @param pdfFirmado
	 * @param processAPI
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM para obtener cedula del firmante
	 * @return String en formato json
	 */
	static String buildPayloadList(String groupName, List<Document> pdfGenerado, List<Document> pdfFirmado,
			ProcessAPI processAPI, IdentityAPI identityAPI, Long idSolicitanteBonitaBPM) {
		if (groupName.isEmpty()) {
			throw new Exception("Se debe enviar el groupName")
		}

		def jsonBuilder = new JsonBuilder()
		List<String> futurePDFName = []
		List<String> futurePDFNames = []
		List<String> pdfEncoded = []
		List<String> pdfEncodeds = []
		String cedula = ""

		try {
			if (groupName.equals(CertificateFields.STUDENT_GROUP_NAME)) {
				// Si el estudiante va a firmar siempre se utiliza el pdfGenerado
				
				logger.info("-----> DOCS GENERADOS "+ pdfGenerado)
				
				futurePDFNames = pdfGenerado.collect{ pdf ->
					return getFutureFilenamePDF(pdf.getContentFileName()); 
				}
				
				logger.info("-----> futurePDFNames "+ futurePDFNames)
				
				pdfEncodeds = pdfGenerado.collect{ pdf ->
					return generarBase64(processAPI, pdf)
				}
				
				futurePDFName.addAll(futurePDFNames)
				pdfEncoded.addAll(pdfEncodeds)
				cedula = getCedula(identityAPI, idSolicitanteBonitaBPM)
			}

			if (groupName.equals(CertificateFields.SECRETARY_GROUP_NAME)) {
				// Si la secretaria va a firmar siempre se utiliza el pdfGenerado
				futurePDFNames = pdfGenerado.collect{ pdf ->
					return getFutureFilenamePDF(pdf.getContentFileName()); 
				}
				
				pdfEncodeds = pdfGenerado.collect{ pdf ->
					return generarBase64(processAPI, pdf)
				}
				
				futurePDFName.addAll(futurePDFNames)
				pdfEncoded.addAll(pdfEncodeds)
				cedula = getCedula(identityAPI, idSolicitanteBonitaBPM)
			}

			if (groupName.equals(CertificateFields.COORDINATION_GROUP_NAME)) {
				if (pdfFirmado == null) {
					futurePDFNames = pdfGenerado.collect{ pdf ->
						return getFutureFilenamePDF(pdf.getContentFileName());
					}
					
					pdfEncodeds = pdfGenerado.collect{ pdf ->
						return generarBase64(processAPI, pdf)
					}
					
					futurePDFName.addAll(futurePDFNames)
					pdfEncoded.addAll(pdfEncodeds)
				} else {
					
					futurePDFNames = pdfFirmado.collect{ pdf ->
						return getFutureFilenamePDF(pdf.getContentFileName());
					}
					
					pdfEncodeds = pdfFirmado.collect{ pdf ->
						return generarBase64(processAPI, pdf)
					}
										
					futurePDFName.addAll(futurePDFNames)
					pdfEncoded.addAll(pdfEncodeds)
				}


				cedula = getCedula(identityAPI, idSolicitanteBonitaBPM)
			}
						
			if (groupName.equals(CertificateFields.DECAN_GROUP_NAME)) {
				// El decano siempre recibe un pdfFirmado
				futurePDFNames = pdfFirmado.collect{ pdf ->
					return getFutureFilenamePDF(pdf.getContentFileName());
				}
				
				pdfEncodeds = pdfFirmado.collect{ pdf ->
					return generarBase64(processAPI, pdf)
				}
										
				futurePDFName.addAll(futurePDFNames)
				pdfEncoded.addAll(pdfEncodeds)
				cedula = getCedula(identityAPI, idSolicitanteBonitaBPM)
			}
			
			def documentos = [] // Creamos una lista vacía para los documentos
			
			// Luego, recorremos las listas futurePDFName y pdfEncoded al mismo tiempo
			// asumiendo que tienen la misma longitud
			futurePDFName.eachWithIndex { nombre, index ->
				def documento = pdfEncoded[index] // Obtenemos el documento correspondiente
				// Creamos un mapa con los atributos "nombre" y "documento"
				def documentoMap = [
					nombre: nombre,
					documento: documento
				]
				documentos.add(documentoMap) // Agregamos el mapa a la lista de documentos
			}
			
			// Ahora usamos la lista de documentos en el jsonBuilder		
			jsonBuilder(cedula: "${cedula}",
			sistema: MPTConstants.SYSTEM_NAME,
			documentos: documentos)
			
			logger.info("-----> PYALOAD LIST "+jsonBuilder.toString())
			return jsonBuilder.toString()

		} catch (Exception e) {
			throw new Exception("Error: " + e)
		}
	}

	/**
	 * Permite codificar un archivo pdf en base64.
	 * 
	 * @param processAPI
	 * @param pdfFile
	 * @return
	 */
	private static String generarBase64(ProcessAPI processAPI, Document pdfFile) {
		if (!pdfFile) {
			throw new Exception("Se debe enviar el archivo pdf")
		}

		byte[] content = processAPI.getDocumentContent(pdfFile.getContentStorageId())
		String encodedFile = Base64.getEncoder().encodeToString(content)

		return encodedFile
	}

	/**
	 * 
	 * @param currentFilename
	 * @return
	 */
	private static String getFutureFilenamePDF(String currentFilename) {
		return currentFilename.replaceAll(MPTConstants.PDF_EXTENSION, MPTConstants.REPLACEMENT_PDF_EXTENSION)
	}
}