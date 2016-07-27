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
package com.sldeditor.ui.detail;

import java.util.List;

import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.RemoteOWS;
import org.geotools.styling.RemoteOWSImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.UserLayer;
import org.geotools.styling.UserLayerImpl;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
import com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUtils;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class UserLayerDetails allows a user to configure user layer data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class UserLayerDetails extends StandardPanel implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public UserLayerDetails(FunctionNameInterface functionManager)
    {
        super(UserLayerDetails.class, functionManager);

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFile(this, "UserLayer.xml");
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        if(selectedSymbol != null)
        {
            StyledLayer styledLayer = selectedSymbol.getStyledLayer();
            if(styledLayer instanceof UserLayerImpl)
            {
                UserLayerImpl userLayer = (UserLayerImpl) styledLayer;

                fieldConfigVisitor.populateTextField(FieldIdEnum.NAME, userLayer.getName());

                // Feature layer constraint
                List<FeatureTypeConstraint> ftcList = userLayer.layerFeatureConstraints();

                fieldConfigVisitor.populateFieldTypeConstraint(FieldIdEnum.LAYER_FEATURE_CONSTRAINTS, ftcList);

                // Source
                GroupConfigInterface group = getGroup(GroupIdEnum.USER_LAYER_SOURCE);
                if(group != null)
                {
                    MultiOptionGroup userLayerSourceGroup = (MultiOptionGroup) group;

                    if(userLayer.getInlineFeatureDatastore() == null)
                    {
                        userLayerSourceGroup.setOption(GroupIdEnum.REMOTE_OWS);

                        // Remote OWS
                        String service = "";
                        String onlineResource = "";
                        RemoteOWS remoteOWS = userLayer.getRemoteOWS();

                        if(remoteOWS != null)
                        {
                            service = remoteOWS.getService();
                            onlineResource = remoteOWS.getOnlineResource();
                        }
                        fieldConfigVisitor.populateTextField(FieldIdEnum.REMOTE_OWS_SERVICE, service);
                        fieldConfigVisitor.populateTextField(FieldIdEnum.REMOTE_OWS_ONLINERESOURCE, onlineResource);
                    }
                    else
                    {
                        userLayerSourceGroup.setOption(GroupIdEnum.INLINE_FEATURE);

                        // Inline features
                        String inlineFeaturesText = InlineFeatureUtils.getInlineFeaturesText(userLayer);
                        fieldConfigVisitor.populateTextField(FieldIdEnum.INLINE_FEATURE, inlineFeaturesText);
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged()
     */
    @Override
    public void dataChanged(FieldId changedField) {
        updateSymbol(changedField);
    }

    /**
     * Update symbol.
     *
     * @param changedField the changed field
     */
    private void updateSymbol(FieldId changedField) {
        if(!Controller.getInstance().isPopulating())
        {
            UserLayer userLayer = getStyleFactory().createUserLayer();
            String name = fieldConfigVisitor.getText(new FieldId(FieldIdEnum.NAME));
            userLayer.setName(name);

            // Feature type constraints
            List<FeatureTypeConstraint> ftcList = fieldConfigVisitor.getFeatureTypeConstraint(new FieldId(FieldIdEnum.LAYER_FEATURE_CONSTRAINTS));
            if((ftcList != null) && !ftcList.isEmpty())
            {
                FeatureTypeConstraint[] ftcArray = new FeatureTypeConstraint[ftcList.size()];
                userLayer.setLayerFeatureConstraints(ftcList.toArray(ftcArray));
            }

            // Source
            GroupConfigInterface group = getGroup(GroupIdEnum.USER_LAYER_SOURCE);
            if(group != null)
            {
                MultiOptionGroup userLayerSourceGroup = (MultiOptionGroup) group;

                OptionGroup selectedOption = userLayerSourceGroup.getSelectedOptionGroup();
                switch(selectedOption.getId())
                {
                case REMOTE_OWS:
                {
                    RemoteOWS remoteOWS = new RemoteOWSImpl();

                    String service = fieldConfigVisitor.getText(FieldIdEnum.REMOTE_OWS_SERVICE);
                    remoteOWS.setService(service);

                    String onlineResource = fieldConfigVisitor.getText(FieldIdEnum.REMOTE_OWS_ONLINERESOURCE);
                    remoteOWS.setOnlineResource(onlineResource);

                    userLayer.setRemoteOWS(remoteOWS);
                }
                break;
                case INLINE_FEATURE:
                {
                    String inlineFeatures = fieldConfigVisitor.getText(FieldIdEnum.INLINE_FEATURE);

                    if((inlineFeatures != null) && (!inlineFeatures.isEmpty()))
                    {
                        InlineFeatureUtils.setInlineFeatures(userLayer, inlineFeatures);
                    }
                }
                break;
                default:
                    break;
                }
            }

            StyledLayer existingStyledLayer = SelectedSymbol.getInstance().getStyledLayer();
            if(existingStyledLayer instanceof UserLayerImpl)
            {
                UserLayerImpl existingUserLayer = (UserLayerImpl) existingStyledLayer;

                for(Style style : existingUserLayer.userStyles())
                {
                    userLayer.addUserStyle(style);
                }
            }
            SelectedSymbol.getInstance().replaceStyledLayer(userLayer);

            // Update inline data sources if the inline data changed, reduces creation of datasources
            if(changedField.getFieldId() == FieldIdEnum.INLINE_FEATURE)
            {
                DataSourceInterface dataSource = DataSourceFactory.getDataSource();
                dataSource.updateUserLayers();
            }

            this.fireUpdateSymbol();
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager()
    {
        return fieldConfigManager;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }
}
