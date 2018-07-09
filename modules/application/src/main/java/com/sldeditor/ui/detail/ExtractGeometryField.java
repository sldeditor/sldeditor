/** */
package com.sldeditor.ui.detail;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigPopulation;
import org.opengis.filter.expression.Expression;

/**
 * Class to extract the geometry field consistently.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExtractGeometryField {

    /**
     * Gets the geometry field.
     *
     * @param fieldConfigVisitor the field config visitor
     * @return the geometry field
     */
    public static Expression getGeometryField(FieldConfigPopulation fieldConfigVisitor) {

        if (fieldConfigVisitor == null) {
            return null;
        }

        Expression geometryExpression = fieldConfigVisitor.getExpression(FieldIdEnum.GEOMETRY);
        if (!validGeometryFieldName(geometryExpression)) {
            return null;
        }

        return geometryExpression;
    }

    /**
     * Check if geometry field name is valid.
     *
     * @param geometryField the geometry field expression
     * @return true, if valid
     */
    private static boolean validGeometryFieldName(Expression geometryField) {
        return ((geometryField != null)
                && (geometryField.toString() != null)
                && !geometryField.toString().trim().isEmpty());
    }
}
