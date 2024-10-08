/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


export interface TransporteInputDTO { 
    matricula?: string;
    estadoTransporte?: TransporteInputDTO.EstadoTransporteEnum;
}
export namespace TransporteInputDTO {
    export type EstadoTransporteEnum = 'DISPONIVEL' | 'EM_ENTREGA' | 'INDISPONIVEL';
    export const EstadoTransporteEnum = {
        Disponivel: 'DISPONIVEL' as EstadoTransporteEnum,
        Em_Entrega: 'EM_ENTREGA' as EstadoTransporteEnum,
        Indisponivel: 'INDISPONIVEL' as EstadoTransporteEnum
    };
}


