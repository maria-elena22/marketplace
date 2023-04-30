package com.fcul.marketplace;

import com.fcul.marketplace.model.Categoria;
import com.fcul.marketplace.model.Propriedade;
import com.fcul.marketplace.model.SubCategoria;
import com.fcul.marketplace.repository.CategoriaRepository;
import com.fcul.marketplace.repository.FornecedorRepository;
import com.fcul.marketplace.repository.SubCategoriaRepository;
import com.fcul.marketplace.repository.TransporteRepository;
import com.fcul.marketplace.repository.UniProdRepository;
import com.fcul.marketplace.service.CategoriaService;
import com.fcul.marketplace.service.TransporteService;
import com.fcul.marketplace.service.UniProdService;
import com.fcul.marketplace.service.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private CategoriaService categoriaService;

    @Override
    public void run(ApplicationArguments args) {



        List<Categoria> categoriaList = categoriaService.getCategorias(null, null, null, null, null);
        if (categoriaList.isEmpty()) {

            DefaultMutableTreeNode appNode = new DefaultMutableTreeNode("App");

            // Mercearia
            DefaultMutableTreeNode merceariaNode = new DefaultMutableTreeNode("Mercearia");
            DefaultMutableTreeNode frutasNode = new DefaultMutableTreeNode("Frutas");
            DefaultMutableTreeNode frutasNacionaisNode = new DefaultMutableTreeNode("Nacional");
            DefaultMutableTreeNode frutasEstrangeirasNode = new DefaultMutableTreeNode("Estrangeiro");
            DefaultMutableTreeNode legumesNode = new DefaultMutableTreeNode("Legumes");
            DefaultMutableTreeNode legumesNacionaisNode = new DefaultMutableTreeNode("Nacional");
            DefaultMutableTreeNode legumesEstrangeirosNode = new DefaultMutableTreeNode("Estrangeiro");
            DefaultMutableTreeNode lacticiniosOvosNode = new DefaultMutableTreeNode("Laticínios e Ovos");
            DefaultMutableTreeNode lacticiniosNode = new DefaultMutableTreeNode("Laticínios");
            DefaultMutableTreeNode ovosNode = new DefaultMutableTreeNode("Ovos");

            frutasNode.add(frutasNacionaisNode);
            frutasNode.add(frutasEstrangeirasNode);
            legumesNode.add(legumesNacionaisNode);
            legumesNode.add(legumesEstrangeirosNode);
            lacticiniosOvosNode.add(lacticiniosNode);
            lacticiniosOvosNode.add(ovosNode);

            merceariaNode.add(frutasNode);
            merceariaNode.add(legumesNode);
            merceariaNode.add(lacticiniosOvosNode);

            // Peixaria
            DefaultMutableTreeNode peixariaNode = new DefaultMutableTreeNode("Peixaria");
            DefaultMutableTreeNode bacalhauNode = new DefaultMutableTreeNode("Bacalhau");
            DefaultMutableTreeNode peixeFrescoNode = new DefaultMutableTreeNode("Peixe Fresco");
            DefaultMutableTreeNode peixeNacionalNode = new DefaultMutableTreeNode("Nacional");
            DefaultMutableTreeNode peixeEstrangeiroNode = new DefaultMutableTreeNode("Estrangeiro");
            DefaultMutableTreeNode peixeCongeladoNode = new DefaultMutableTreeNode("Peixe Congelado");
            DefaultMutableTreeNode polvoLulasChocosNode = new DefaultMutableTreeNode("Polvo, Lulas e Chocos");
            DefaultMutableTreeNode polvoNode = new DefaultMutableTreeNode("Polvo");
            DefaultMutableTreeNode lulasNode = new DefaultMutableTreeNode("Lulas");
            DefaultMutableTreeNode chocosNode = new DefaultMutableTreeNode("Chocos");
            DefaultMutableTreeNode mariscoNode = new DefaultMutableTreeNode("Marisco");
            DefaultMutableTreeNode camaraoNode = new DefaultMutableTreeNode("Camarão");
            DefaultMutableTreeNode outroMariscoNode = new DefaultMutableTreeNode("Outro marisco");

            peixeFrescoNode.add(peixeNacionalNode);
            peixeFrescoNode.add(peixeEstrangeiroNode);
            polvoLulasChocosNode.add(polvoNode);
            polvoLulasChocosNode.add(lulasNode);
            polvoLulasChocosNode.add(chocosNode);
            mariscoNode.add(camaraoNode);
            mariscoNode.add(outroMariscoNode);

            peixariaNode.add(bacalhauNode);
            peixariaNode.add(peixeFrescoNode);
            peixariaNode.add(peixeCongeladoNode);
            peixariaNode.add(polvoLulasChocosNode);
            peixariaNode.add(mariscoNode);

            // Talho
            DefaultMutableTreeNode talhoNode = new DefaultMutableTreeNode("Talho");
            DefaultMutableTreeNode novilhoVitelaVitelaoNode = new DefaultMutableTreeNode("Novilho, Vitela e Vitelão");
            DefaultMutableTreeNode novilhoNode = new DefaultMutableTreeNode("Novilho");
            DefaultMutableTreeNode vitelaNode = new DefaultMutableTreeNode("Vitela");
            DefaultMutableTreeNode vitelaoNode = new DefaultMutableTreeNode("Vitelão");
            DefaultMutableTreeNode frangoPeruNode = new DefaultMutableTreeNode("Frango e Peru");
            DefaultMutableTreeNode frangoNode = new DefaultMutableTreeNode("Frango");
            DefaultMutableTreeNode peruNode = new DefaultMutableTreeNode("Peru");
            DefaultMutableTreeNode porcoNode = new DefaultMutableTreeNode("Porco");
            DefaultMutableTreeNode patoCoelhoNode = new DefaultMutableTreeNode("Pato e Coelho");
            DefaultMutableTreeNode patoNode = new DefaultMutableTreeNode("Pato");
            DefaultMutableTreeNode coelhoNode = new DefaultMutableTreeNode("Coelho");
            DefaultMutableTreeNode cabritoBorregoNode = new DefaultMutableTreeNode("Cabrito e Borrego");
            DefaultMutableTreeNode cabritoNode = new DefaultMutableTreeNode("Cabrito");
            DefaultMutableTreeNode borregoNode = new DefaultMutableTreeNode("Borrego");

            novilhoVitelaVitelaoNode.add(novilhoNode);
            novilhoVitelaVitelaoNode.add(vitelaNode);
            novilhoVitelaVitelaoNode.add(vitelaoNode);
            frangoPeruNode.add(frangoNode);
            frangoPeruNode.add(peruNode);
            patoCoelhoNode.add(patoNode);
            patoCoelhoNode.add(coelhoNode);
            cabritoBorregoNode.add(cabritoNode);
            cabritoBorregoNode.add(borregoNode);

            talhoNode.add(novilhoVitelaVitelaoNode);
            talhoNode.add(frangoPeruNode);
            talhoNode.add(porcoNode);
            talhoNode.add(patoCoelhoNode);
            talhoNode.add(cabritoBorregoNode);

            // Congelados
            DefaultMutableTreeNode congeladosNode = new DefaultMutableTreeNode("Congelados");
            DefaultMutableTreeNode geladosNode = new DefaultMutableTreeNode("Gelados");
            DefaultMutableTreeNode frutasLegumesNode = new DefaultMutableTreeNode("Frutas e Legumes");
            DefaultMutableTreeNode frutaNode = new DefaultMutableTreeNode("Fruta");
            DefaultMutableTreeNode legumesCongeladosNode = new DefaultMutableTreeNode("Legumes");
            DefaultMutableTreeNode batatasCongeladasNode = new DefaultMutableTreeNode("Batatas Congeladas");
            DefaultMutableTreeNode salgadosAperitivosNode = new DefaultMutableTreeNode("Salgados e Aperitivos");
            DefaultMutableTreeNode salgadosNode = new DefaultMutableTreeNode("Salgados");
            DefaultMutableTreeNode aperitivosNode = new DefaultMutableTreeNode("Aperitivos");
            DefaultMutableTreeNode pizzasNode = new DefaultMutableTreeNode("Pizzas");
            DefaultMutableTreeNode hamburgueresRefeicoesNode = new DefaultMutableTreeNode("Hambúrgueres e Refeições");
            DefaultMutableTreeNode hamburgueresNode = new DefaultMutableTreeNode("Hambúrgueres");
            DefaultMutableTreeNode refeicoesNode = new DefaultMutableTreeNode("Refeições");
            DefaultMutableTreeNode carneCongeladaNode = new DefaultMutableTreeNode("Carne Congelada");
            DefaultMutableTreeNode peixeMariscoCongeladoNode = new DefaultMutableTreeNode("Peixe e Marisco Congelado");

            frutasLegumesNode.add(frutaNode);
            frutasLegumesNode.add(legumesCongeladosNode);
            salgadosAperitivosNode.add(salgadosNode);
            salgadosAperitivosNode.add(aperitivosNode);
            hamburgueresRefeicoesNode.add(hamburgueresNode);
            hamburgueresRefeicoesNode.add(refeicoesNode);

            congeladosNode.add(geladosNode);
            congeladosNode.add(frutasLegumesNode);
            congeladosNode.add(batatasCongeladasNode);
            congeladosNode.add(salgadosAperitivosNode);
            congeladosNode.add(pizzasNode);
            congeladosNode.add(hamburgueresRefeicoesNode);
            congeladosNode.add(carneCongeladaNode);
            congeladosNode.add(peixeMariscoCongeladoNode);

            // Limpeza
            DefaultMutableTreeNode limpezaNode = new DefaultMutableTreeNode("Limpeza");
            DefaultMutableTreeNode limpezaGeralNode = new DefaultMutableTreeNode("Limpeza Geral");
            DefaultMutableTreeNode limpezaCozinhaNode = new DefaultMutableTreeNode("Limpeza da Cozinha");
            DefaultMutableTreeNode detergenteLoicaNode = new DefaultMutableTreeNode("Detergente Loiça");
            DefaultMutableTreeNode limpezaFogaoFornoFrigorificoNode = new DefaultMutableTreeNode("Limpeza Fogão, Forno e Frigorífico");
            DefaultMutableTreeNode limpezaCasaBanhoNode = new DefaultMutableTreeNode("Limpeza Casa de Banho");
            DefaultMutableTreeNode lavaTudoNode = new DefaultMutableTreeNode("Lava Tudo");
            DefaultMutableTreeNode desentupidoresNode = new DefaultMutableTreeNode("Desentupidores");
            DefaultMutableTreeNode blocosSanitariosNode = new DefaultMutableTreeNode("Blocos Sanitários");
            DefaultMutableTreeNode ambientadoresNode = new DefaultMutableTreeNode("Ambientadores");
            DefaultMutableTreeNode ambientadoresAutomaticosManuaisNode = new DefaultMutableTreeNode("Ambientadores automáticos e manuais");
            DefaultMutableTreeNode ambientadoresAutomaticosNode = new DefaultMutableTreeNode("Ambientadores automáticos");
            DefaultMutableTreeNode ambientadoresManuaisNode = new DefaultMutableTreeNode("Ambientadores manuais");
            DefaultMutableTreeNode spraysNode = new DefaultMutableTreeNode("Sprays");
            DefaultMutableTreeNode velasNode = new DefaultMutableTreeNode("Velas");
            DefaultMutableTreeNode guardanaposRolosPeliculasNode = new DefaultMutableTreeNode("Guardanapos, Rolos e Películas");
            DefaultMutableTreeNode desinfetantesProtecaoNode = new DefaultMutableTreeNode("Desinfetantes e Proteção");

            limpezaCozinhaNode.add(detergenteLoicaNode);
            limpezaCozinhaNode.add(limpezaFogaoFornoFrigorificoNode);
            limpezaCasaBanhoNode.add(lavaTudoNode);
            limpezaCasaBanhoNode.add(desentupidoresNode);
            limpezaCasaBanhoNode.add(blocosSanitariosNode);
            ambientadoresAutomaticosManuaisNode.add(ambientadoresAutomaticosNode);
            ambientadoresAutomaticosManuaisNode.add(ambientadoresManuaisNode);
            ambientadoresNode.add(ambientadoresAutomaticosManuaisNode);
            ambientadoresNode.add(spraysNode);
            ambientadoresNode.add(velasNode);

            limpezaNode.add(limpezaGeralNode);
            limpezaNode.add(limpezaCozinhaNode);
            limpezaNode.add(limpezaCasaBanhoNode);
            limpezaNode.add(ambientadoresNode);
            limpezaNode.add(guardanaposRolosPeliculasNode);
            limpezaNode.add(desinfetantesProtecaoNode);

            // Eletrodomésticos
            DefaultMutableTreeNode eletrodomesticosNode = new DefaultMutableTreeNode("Eletrodomésticos");
            DefaultMutableTreeNode cozinhaNode = new DefaultMutableTreeNode("Cozinha");
            DefaultMutableTreeNode frigorificosNode = new DefaultMutableTreeNode("Frigoríficos");
            DefaultMutableTreeNode microondasFogoesNode = new DefaultMutableTreeNode("Microondas e fogões");
            DefaultMutableTreeNode maquinasCafeNode = new DefaultMutableTreeNode("Máquinas de café");
            DefaultMutableTreeNode salaNode = new DefaultMutableTreeNode("Sala");
            DefaultMutableTreeNode arCondicionadoNode = new DefaultMutableTreeNode("Ar condicionado");
            DefaultMutableTreeNode ventoinhasNode = new DefaultMutableTreeNode("Ventoinhas");
            DefaultMutableTreeNode televisoesNode = new DefaultMutableTreeNode("Televisões");
            DefaultMutableTreeNode casaBanhoNode = new DefaultMutableTreeNode("Casa de Banho");
            DefaultMutableTreeNode secadorCabeloNode = new DefaultMutableTreeNode("Secador de cabelo");
            DefaultMutableTreeNode maquinasBarbearNode = new DefaultMutableTreeNode("Máquinas de barbear");
            DefaultMutableTreeNode escovasDentesNode = new DefaultMutableTreeNode("Escovas de dentes");

            cozinhaNode.add(frigorificosNode);
            cozinhaNode.add(microondasFogoesNode);
            cozinhaNode.add(maquinasCafeNode);
            salaNode.add(arCondicionadoNode);
            salaNode.add(ventoinhasNode);
            salaNode.add(televisoesNode);
            casaBanhoNode.add(secadorCabeloNode);
            casaBanhoNode.add(maquinasBarbearNode);
            casaBanhoNode.add(escovasDentesNode);

            eletrodomesticosNode.add(cozinhaNode);
            eletrodomesticosNode.add(salaNode);
            eletrodomesticosNode.add(casaBanhoNode);

            // Add sub-nodes to the root node
            appNode.add(merceariaNode);
            appNode.add(peixariaNode);
            appNode.add(talhoNode);
            appNode.add(congeladosNode);
            appNode.add(limpezaNode);
            appNode.add(eletrodomesticosNode);

            applyFunctionToTree(appNode,null,null);


            Categoria categoria = categoriaService.getCategorias("Mercearia",null,null,null,null).get(0);
            Categoria categoria2 = categoriaService.getCategorias("Peixaria",null,null,null,null).get(0);
            Categoria categoria3 = categoriaService.getCategorias("Talho",null,null,null,null).get(0);
            Categoria categoria4 = categoriaService.getCategorias("Congelados",null,null,null,null).get(0);
            Categoria categoria5 = categoriaService.getCategorias("Limpeza",null,null,null,null).get(0);
            Categoria categoria6 = categoriaService.getCategorias("Eletrodomésticos",null,null,null,null).get(0);


            Propriedade propriedade = new Propriedade();
            propriedade.setNomePropriedade("Prazo de validade");

            Propriedade propriedade2 = new Propriedade();
            propriedade2.setNomePropriedade("Contem gluten");

            Propriedade propriedade3 = new Propriedade();
            propriedade3.setNomePropriedade("Indice energetico");

            Propriedade propriedade4 = new Propriedade();
            propriedade4.setNomePropriedade("Conselhos de utilização");

            categoriaService.addPropriedade(categoria.getIdCategoria(),propriedade);
            categoriaService.addPropriedade(categoria.getIdCategoria(),propriedade2);
            categoriaService.addPropriedade(categoria2.getIdCategoria(),propriedade);
            categoriaService.addPropriedade(categoria3.getIdCategoria(),propriedade);


            categoriaService.addPropriedade(categoria4.getIdCategoria(),propriedade);
            categoriaService.addPropriedade(categoria5.getIdCategoria(),propriedade4);

            categoriaService.addPropriedade(categoria6.getIdCategoria(),propriedade3);
        }

    }

    public void printTreeNode(DefaultMutableTreeNode node,int level) {
        // Print the node's user object
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<level;i++){
            sb.append("\t");
        }
        System.out.println(sb.toString() + node.getUserObject().toString());

        // Recursively print the children
        for (Enumeration e = node.children(); e.hasMoreElements();) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) e.nextElement();
            printTreeNode(child,level+1);
        }
    }

    private Categoria criaCategoria(final String nome) {
        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(nome);
        return categoriaService.addCategoria(categoria);
    }

    private SubCategoria criaSubCategoria(final Categoria categoria, final SubCategoria subCategoria,final String nome) {
        SubCategoria subCategoria2 = new SubCategoria();
        subCategoria2.setNomeSubCategoria(nome);
        return categoriaService.addSubCategoria(subCategoria2, subCategoria!=null?subCategoria.getIdSubCategoria():null, categoria.getIdCategoria());
    }



    public void applyFunctionToTree(DefaultMutableTreeNode rootNode,Categoria categoria,SubCategoria subCategoria) {

        // get the children of the root node
        Enumeration<?> children = rootNode.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            String nodeName = child.getUserObject().toString();

            if (categoria == null) {
                // create a new Categoria object
                Categoria newCategoria = criaCategoria(nodeName);
                applyFunctionToTree(child,newCategoria,null);
            } else {
                SubCategoria newSubCategoria;
                if (subCategoria == null) {
                    // create a new SubCategoria object
                    newSubCategoria = criaSubCategoria(categoria, null, nodeName);
                } else {
                    // create a new SubCategoria object with subCategoria from previous level
                    newSubCategoria = criaSubCategoria(categoria, subCategoria, nodeName);
                }
                applyFunctionToTree(child,categoria,newSubCategoria);
            }
        }
    }


}
