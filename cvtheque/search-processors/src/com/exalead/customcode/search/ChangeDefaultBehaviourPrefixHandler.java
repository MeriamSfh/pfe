package com.exalead.customcode.search;

import com.exalead.config.bean.PropertyLabel;
import com.exalead.mercury.component.CVComponent;
import com.exalead.mercury.component.CVComponentDescription;
import com.exalead.mercury.component.config.CVComponentConfigClass;
import com.exalead.search.query.QueryContext;
import com.exalead.search.query.QueryProcessingException;
import com.exalead.search.query.node.And;
import com.exalead.search.query.node.FuzzyAnd;
import com.exalead.search.query.node.NaryNode;
import com.exalead.search.query.node.Node;
import com.exalead.search.query.node.NodeVisitor;
import com.exalead.search.query.node.Or;
import com.exalead.search.query.node.PrefixNode;
import com.exalead.search.query.node.UserQueryChunk;
import com.exalead.search.query.node.Xor;
import com.exalead.search.query.prefix.CustomPrefixHandler;
import com.exalead.search.query.util.NodeInspector;

/**
 * The default behaviour of Cloudview for a search is to use the AND operator between words. 
 * In this sample, the goal of the prefix handler is to be able to change the   
 * this behaviour by its usage with a specific operator (XOR, OR, AND or FUZZYAND): 
 *  
 *  Usage of AND, OR or XOR are the same but as FUZZYAND uses a ratio of minimum term to match a document, 
 *  configuration of this prefix handler allows to specify as operator:
 *      - FUZZYAND/d with d a positive or negative number :
 *          => FUZZYAND/2 means at least two of those words
 *          => FUZZYAND/-1 means all words but I allow a miss of one of them
 *      - FUZZYAND :
 *          in this case, prefix handler will ask for a minimum of half words
 */
@PropertyLabel(value = "Change Behaviour")
@CVComponentConfigClass(configClass=ChangeDefaultBehaviourPrefixHandlerConfig.class)
@CVComponentDescription("(Sample) This prefix handler allows to change the default behaviour by using another operator rather than the default one.")
public class ChangeDefaultBehaviourPrefixHandler extends CustomPrefixHandler implements CVComponent{
    
    
    private String operator = null;
    
    public ChangeDefaultBehaviourPrefixHandler(ChangeDefaultBehaviourPrefixHandlerConfig config) {
        super(config);
        operator = config.getOperator();
    }


    public void addChildren(NaryNode newNode, String[] queryParts){
          
    }
    
    @Override
    public Node handlePrefix(Phase phase, PrefixNode node, NodeVisitor parentVisitor,
            QueryContext queryContext) throws QueryProcessingException {
        
        if(phase.equals(Phase.PRE_LINGUISTIC) ){
            int slashPos = -1; 
            String queryContent = NodeInspector.concatenatedContent(node).value;
            String[] queryParts =queryContent.split(" ");
            NaryNode newNode = null;
            if (operator.contains("XOR")){
                newNode = new Xor();
            }else if (operator.contains("OR")){
                newNode = new Or();
            }else if (operator.contains("FUZZYAND")){
                newNode = new FuzzyAnd();
                slashPos = operator.indexOf("/");
                if(slashPos<0){
                    ((FuzzyAnd)newNode).minSuccess = (int)queryParts.length/2;
                }else if(operator.charAt(slashPos+1)=='-'){
                    ((FuzzyAnd)newNode).maxFailure=new Integer(operator.substring(slashPos+2));
                }else {
                    ((FuzzyAnd)newNode).minSuccess=new Integer(operator.substring(slashPos+1));
                }
            } else {
                newNode = new And();
            }
            for (int i=1; i<queryParts.length; i++){
                newNode.addChild(new UserQueryChunk(queryParts[i]));
            } 
            return newNode; 
        }
        return node;
    }
}

