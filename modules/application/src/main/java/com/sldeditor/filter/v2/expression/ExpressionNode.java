/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2016, SCISYS UK Limited
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sldeditor.filter.v2.expression;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface;
import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.MathExpressionImpl;
import org.geotools.filter.expression.AddImpl;
import org.geotools.filter.expression.DivideImpl;
import org.geotools.filter.expression.MultiplyImpl;
import org.geotools.filter.expression.SubtractImpl;
import org.geotools.filter.function.EnvFunction;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.parameter.Parameter;

/**
 * The Class ExpressionNode.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExpressionNode extends DefaultMutableTreeNode {

    /** The expression. */
    private transient Expression expression = null;

    /** The type. */
    private Class<?> type = String.class;

    /** The name. */
    private String name = null;

    /** The display string. */
    private String displayString = "";

    /** The value only flag. */
    private boolean valueOnly = false;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The expression type. */
    private ExpressionTypeEnum expressionType = ExpressionTypeEnum.EXPRESSION;

    /** The optional parameter. */
    private boolean optionalParam = false;

    /** The optional parameter used. */
    private boolean optionalParamUsed = false;

    /** The expression node parameter. */
    private transient Parameter<?> expressionNodeParameter = null;

    /** The Constant UNLIMITED_STRING_SIZE. */
    public static final int UNLIMITED_STRING_SIZE = -1;

    /** The maximum string size value (initialised to unlimited). */
    private int maxStringSize = UNLIMITED_STRING_SIZE;

    /** The regular expression string flag. */
    private boolean regExpString = false;

    /** The math expression map. */
    private static Map<Class<?>, String> mathExpressionMap = new HashMap<Class<?>, String>();

    /** The environment manager. */
    private static EnvironmentManagerInterface envMgr = null;

    /** Instantiates a new expression node. */
    public ExpressionNode() {
        setDisplayString();
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return displayString;
    }

    /** Sets the display string. */
    public void setDisplayString() {
        StringBuilder sb = new StringBuilder();

        if (name != null) {
            sb.append(name);
            sb.append(" : ");
        }

        if (expression == null) {
            if (expressionType == ExpressionTypeEnum.LITERAL) {
                sb.append(
                        Localisation.getString(
                                ExpressionPanelv2.class, "ExpressionPanelv2.literalNotSet"));
            } else if (expressionType == ExpressionTypeEnum.PROPERTY) {
                sb.append(
                        Localisation.getString(
                                ExpressionPanelv2.class, "ExpressionPanelv2.propertyNotSet"));
            } else {
                if (optionalParam) {
                    sb.append(
                            Localisation.getString(
                                    ExpressionPanelv2.class, "ExpressionPanelv2.optional"));

                } else {
                    sb.append(
                            Localisation.getString(
                                    ExpressionPanelv2.class, "ExpressionPanelv2.expressionNotSet"));
                }
            }
        }

        if (expression instanceof LiteralExpressionImpl) {
            sb.append(
                    Localisation.getField(ExpressionPanelv2.class, "ExpressionPanelv2.literal")
                            + " ");
            sb.append(expression.toString());
        } else if (expression instanceof AttributeExpressionImpl) {
            sb.append(
                    String.format(
                            "%s : [%s]",
                            Localisation.getString(
                                    ExpressionPanelv2.class, "ExpressionPanelv2.attribute"),
                            expression.toString()));
        } else if (expression instanceof FunctionExpressionImpl) {
            sb.append(FunctionExpressionUtils.toString(expression));
        } else if (expression instanceof FunctionImpl) {
            sb.append(expression.toString());
        } else if (expression instanceof Function) {
            sb.append(FunctionInterfaceUtils.toString(expression));
        } else if (expression instanceof MathExpressionImpl) {
            if (mathExpressionMap.isEmpty()) {
                mathExpressionMap.put(AddImpl.class, "+");
                mathExpressionMap.put(SubtractImpl.class, "-");
                mathExpressionMap.put(DivideImpl.class, "/");
                mathExpressionMap.put(MultiplyImpl.class, "*");
            }

            String str = mathExpressionMap.get(expression.getClass());
            sb.append(str);
        }

        displayString = sb.toString();
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        return expression;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type to set
     */
    public void setType(Class<?> type) {
        this.type = type;
    }

    /**
     * Sets the expression.
     *
     * @param expression the expression to set
     */
    public void setExpression(Expression expression) {
        this.expression = expression;
        if (expression instanceof LiteralExpressionImpl) {
            Object value = ((LiteralExpressionImpl) expression).getValue();
            if (value instanceof AttributeExpressionImpl) {
                this.expression = (AttributeExpressionImpl) value;
            }
        }

        setDisplayString();

        this.removeAllChildren();

        if (this.expression instanceof EnvFunction) {
            EnvFunction envVarExpression = (EnvFunction) this.expression;
            ExpressionNode childNode = new ExpressionNode();
            childNode.setExpressionType(ExpressionTypeEnum.ENVVAR);

            Expression envVarLiteral = envVarExpression.getParameters().get(0);
            Class<?> dataType = Object.class;

            if (envMgr != null) {
                dataType = envMgr.getDataType(envVarLiteral);
            }
            childNode.setType(dataType);
            childNode.setName(
                    Localisation.getString(ExpressionPanelv2.class, "ExpressionPanelv2.envVar"));

            childNode.setExpression(envVarLiteral);

            this.insert(childNode, this.getChildCount());
        } else if (this.expression instanceof FunctionExpression) {
            FunctionExpression functionExpression = (FunctionExpression) this.expression;
            FunctionName functionName = functionExpression.getFunctionName();

            TypeManager.getInstance().setDataType(functionName.getReturn().getType());
            int argCount = functionName.getArgumentCount();

            if (functionName.getArgumentCount() < 0) {
                argCount *= -1;
            }

            for (int index = 0;
                    index < Math.max(functionExpression.getParameters().size(), argCount);
                    index++) {
                ExpressionNode childNode = new ExpressionNode();

                // If function has a variable number of arguments pick the last one
                int argumentIndex = Math.min(index, Math.max(0, argCount - 1));

                Parameter<?> parameter = functionName.getArguments().get(argumentIndex);
                childNode.setParameter(parameter);

                if (index < functionExpression.getParameters().size()) {
                    childNode.setExpression(functionExpression.getParameters().get(index));
                } else {
                    childNode.setExpression(null);
                }
                this.insert(childNode, this.getChildCount());
            }
        } else if (this.expression instanceof FunctionImpl) {
            FunctionImpl functionExpression = (FunctionImpl) this.expression;
            FunctionName functionName = functionExpression.getFunctionName();

            TypeManager.getInstance().setDataType(functionName.getReturn().getType());

            int maxArgument = Math.abs(functionName.getArgumentCount());

            for (int index = 0; index < maxArgument; index++) {
                ExpressionNode childNode = new ExpressionNode();
                Parameter<?> parameter = functionName.getArguments().get(0);
                childNode.setType(parameter.getType());
                childNode.setName(parameter.getName());

                if (index < functionExpression.getParameters().size()) {
                    childNode.setExpression(functionExpression.getParameters().get(index));
                }
                this.insert(childNode, this.getChildCount());
            }
        } else if (this.expression instanceof Function) {
            Function functionExpression = (Function) this.expression;
            FunctionName functionName = functionExpression.getFunctionName();

            TypeManager.getInstance().setDataType(functionName.getReturn().getType());

            int overallIndex = 0;
            for (Parameter<?> param : functionName.getArguments()) {
                if (param.getMinOccurs() == 0) {
                    ExpressionNode childNode = new ExpressionNode();
                    childNode.setParameter(param);
                    childNode.setOptional();

                    if (overallIndex < functionExpression.getParameters().size()) {
                        childNode.setExpression(
                                functionExpression.getParameters().get(overallIndex));
                        childNode.setOptionalParamUsed(true);
                    } else {
                        childNode.setExpression(null);
                    }
                    overallIndex++;
                    this.insert(childNode, this.getChildCount());
                } else {
                    for (int index = 0; index < param.getMinOccurs(); index++) {
                        ExpressionNode childNode = new ExpressionNode();
                        childNode.setParameter(param);

                        if (index < functionExpression.getParameters().size()) {
                            if (overallIndex < functionExpression.getParameters().size()) {
                                childNode.setExpression(
                                        functionExpression.getParameters().get(overallIndex));
                            } else {
                                childNode.setExpression(null);
                            }
                        }

                        overallIndex++;
                        this.insert(childNode, this.getChildCount());
                    }
                }
            }
        } else if (expression instanceof MathExpressionImpl) {
            MathExpressionImpl mathsExpression = (MathExpressionImpl) expression;

            String expressionText =
                    Localisation.getString(ExpressionPanelv2.class, "ExpressionPanelv2.expression");
            ExpressionNode childNode1 = new ExpressionNode();
            childNode1.setType(Number.class);
            childNode1.setName(expressionText + " 1");
            childNode1.setExpression(mathsExpression.getExpression1());
            this.insert(childNode1, this.getChildCount());

            ExpressionNode childNode2 = new ExpressionNode();
            childNode2.setType(Number.class);
            childNode2.setName(expressionText + " 2");
            childNode2.setExpression(mathsExpression.getExpression2());
            this.insert(childNode2, this.getChildCount());
        } else if (expression instanceof AttributeExpressionImpl) {
            @SuppressWarnings("unused")
            AttributeExpressionImpl property = (AttributeExpressionImpl) expression;

            // TypeManager.getInstance().setLiteralType(literal.getValue().getClass());
        } else if (expression instanceof LiteralExpressionImpl) {
            LiteralExpressionImpl literal = (LiteralExpressionImpl) expression;
            if (literal.getValue() != null) {
                TypeManager.getInstance().setDataType(literal.getValue().getClass());
            }
        }
    }

    /** Marks the parameter as optional. */
    private void setOptional() {
        optionalParam = true;
    }

    /**
     * Sets the data from a parameter.
     *
     * @param parameter the new parameter
     */
    public void setParameter(Parameter<?> parameter) {
        expressionNodeParameter = parameter;
        if (parameter != null) {
            setType(parameter.getType());
            setName(parameter.getName());
        }
    }

    /**
     * Gets the parameter.
     *
     * @return the parameter
     */
    public Parameter<?> getParameter() {
        return expressionNodeParameter;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;

        setDisplayString();
    }

    /**
     * Checks if is value only.
     *
     * @return the valueOnly
     */
    public boolean isValueOnly() {
        return valueOnly;
    }

    /**
     * Sets the value only.
     *
     * @param valueOnly the valueOnly to set
     */
    public void setValueOnly(boolean valueOnly) {
        this.valueOnly = valueOnly;
    }

    /**
     * Gets the expression type.
     *
     * @return the expressionType
     */
    public ExpressionTypeEnum getExpressionType() {
        return expressionType;
    }

    /**
     * Sets the expression type.
     *
     * @param expressionType the expressionType to set
     */
    public void setExpressionType(ExpressionTypeEnum expressionType) {
        this.expressionType = expressionType;
        setDisplayString();
    }

    /**
     * Sets the env mgr.
     *
     * @param envMgr the new env mgr
     */
    public static void setEnvMgr(EnvironmentManagerInterface envMgr) {
        ExpressionNode.envMgr = envMgr;
    }

    /**
     * Checks if is optional param.
     *
     * @return the optionalParam
     */
    public boolean isOptionalParam() {
        return optionalParam;
    }

    /**
     * Checks if is optional param used.
     *
     * @return the optionalParamUsed
     */
    public boolean isOptionalParamUsed() {
        return optionalParamUsed;
    }

    /**
     * Sets the optional param used.
     *
     * @param optionalParamUsed the optionalParamUsed to set
     */
    public void setOptionalParamUsed(boolean optionalParamUsed) {
        this.optionalParamUsed = optionalParamUsed;
    }

    /**
     * Gets the max string size.
     *
     * @return the maxStringSize
     */
    public int getMaxStringSize() {
        return maxStringSize;
    }

    /**
     * Sets the max characters.
     *
     * @param maxStringSize the maximum string size to set
     */
    public void setMaxStringSize(int maxStringSize) {
        this.maxStringSize = maxStringSize;
    }

    /**
     * Returns is regular expression string flag.
     *
     * @return the regExpString
     */
    public boolean isRegExpString() {
        return regExpString;
    }

    /**
     * Sets the regular expression string flag.
     *
     * @param regExpString the regExpString to set
     */
    public void setRegExpString(boolean regExpString) {
        this.regExpString = regExpString;
    }
}
