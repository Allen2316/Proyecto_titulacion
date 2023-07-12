/**
 * Función que permite verificar si el existe el pdf firmado en un servidor web 
 * nediante su ruta absoluta o url y de acuerdo al código de respuesta recibido 
 * abrirá el modal correspondiente.
 * 
 * @param {*} $scope
 * @param {*} $http
 * @param {*} $window
 * @param {*} modalService
 */ 
function PbButtonPdfCtrl($scope, $http, $window, modalService) {
    'use strict'

    var vm = this

    this.action = function action() {
        doRequest($scope.properties.action, $scope.properties.url)
    }

    /**
     * Ejecuta una petición get/post a una URL.
     * También vincula datos personalizados de éxito|error a un dato.
     * 
     * @return {void}
     */
    function doRequest(method, url, params) {
        vm.busy = true
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params,
        }

        return $http(req)
            .success(function (data, status) {
                $scope.properties.dataFromSuccess = data
                $scope.properties.responseStatusCode = status
                $scope.properties.dataFromError = undefined
                notifyParentFrame({
                    message: 'success',
                    status: status,
                    dataFromSuccess: data,
                    dataFromError: undefined,
                    responseStatusCode: status,
                })
            })
            .error(function (data, status) {
                $scope.properties.dataFromError = data
                $scope.properties.responseStatusCode = status
                $scope.properties.dataFromSuccess = undefined
                notifyParentFrame({
                    message: 'error',
                    status: status,
                    dataFromError: data,
                    dataFromSuccess: undefined,
                    responseStatusCode: status,
                })
            })
            .finally(function () {
                vm.busy = false

                var modalIden = ''
                if ($scope.properties.responseStatusCode == 200) {
                    modalIden = 'modalEstaPdfFirmado'
                } else if ($scope.properties.responseStatusCode == 404) {
                    $scope.properties.mensajeParaElModal = 'El documento aún no ha sido firmado'
                    modalIden = 'modalWarnings'
                } else {
                    $scope.properties.mensajeParaElModal = 'No es posible conectarse con el servidor web'
                    modalIden = 'modalWarnings'
                }
                openModal(modalIden)
            })
            
    }

    function notifyParentFrame(additionalProperties) {
        if ($window.parent !== $window.self) {
            var dataToSend = angular.extend(
                {},
                $scope.properties,
                additionalProperties
            )
            $window.parent.postMessage(JSON.stringify(dataToSend), '*')
        }
    }

    function openModal(modalId) {
        modalService.open(modalId)
    }

    function closeModal(shouldClose) {
        if (shouldClose) modalService.close()
    }
}
