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
import { ProdutoUniProdDTO } from '../uniProd/produtoUniProdDTO';
import { SubCategoriaDTO } from '../categoria/subCategoriaDTO';
import { ProdutoFornecedorInfoDTO } from '../produto/produtoFornecedorInfoDTO';


export interface FullProdutoDTO { 
    idProduto?: number;
    nome?: string;
    descricao?: string;
    iva?: FullProdutoDTO.IvaEnum;
    uniProds?: Array<ProdutoUniProdDTO>;
    subCategorias?: Array<SubCategoriaDTO>;
    propriedades?: { [key: string]: string; };
    precoFornecedores?: Array<ProdutoFornecedorInfoDTO>;
}
export namespace FullProdutoDTO {
    export type IvaEnum = 'IVA_6' | 'IVA_13' | 'IVA_23';
    export const IvaEnum = {
        _6: 'IVA_6' as IvaEnum,
        _13: 'IVA_13' as IvaEnum,
        _23: 'IVA_23' as IvaEnum
    };
}


