package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiStatement;
import com.u3.filechains.ClickMehtod;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActivityStrategy extends GenCodeStrategy{
    public ActivityStrategy(List<String> code, Map<ClickMehtod,List<String>> clickMap){
        super(code,clickMap);
    }
    @Override
    public void genFindView(PsiClass mClass, PsiElementFactory mFactory) {
        PsiStatement statement = findSetContentView(mClass);
        insertFindViewCode(mClass,mFactory,statement,code);
    }
    private PsiStatement findSetContentView(PsiClass mClass){
        PsiStatement result = null;
        PsiMethod onCreate = mClass.findMethodsByName("onCreate", false)[0];
        for (PsiStatement statement : onCreate.getBody().getStatements()) {
            if (statement.getFirstChild() instanceof PsiMethodCallExpression) {
                PsiReferenceExpression methodExpression
                        = ((PsiMethodCallExpression) statement.getFirstChild())
                        .getMethodExpression();
                if (methodExpression.getText().equals("setContentView")) {
                    result = statement;
                    break;
                }
            }
        }
        return result;
    }
    @Override
    public void genOnClick(PsiClass mClass, PsiElementFactory mFactory) {
        PsiStatement statement = findSetContentView(mClass);
        for (ClickMehtod method:clickMap.keySet()){
            StringBuilder methodString = getMethodInvokeString(method);
            for(String id:clickMap.get(method)){
                String code = getOnClickCode(methodString, id);
                insertOnclickCode(mClass, mFactory, statement, code);
            }
        }
    }
}
