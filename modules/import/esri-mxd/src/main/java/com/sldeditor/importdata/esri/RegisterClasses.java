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
package com.sldeditor.importdata.esri;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.sldeditor.importdata.esri.label.EsriLabelRendererInterface;
import com.sldeditor.importdata.esri.numberformat.EsriNumberFormatInterface;
import com.sldeditor.importdata.esri.renderer.EsriRendererInterface;
import com.sldeditor.importdata.esri.symbol.EsriSymbolInterface;
import com.sldeditor.importdata.esri.textbackground.EsriTextBackgroundInterface;

/**
 * The Class RegisterClasses.
 *
 * @author Robert Ward (SCISYS)
 */
public class RegisterClasses {

    private static final String PACKAGE_INFO = "package-info";
    /** The logger. */
    private static Logger logger = Logger.getLogger(RegisterClasses.class);

    /**
     * Initialise.
     *
     * @param data the data
     */
    public static void initialise(ConversionData data)
    {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        registerRenderers(classLoadersList, data);
        registerSymbols(classLoadersList, data);
        registerLabelRenderers(classLoadersList, data);
        registerTextBackground(classLoadersList, data);
        registerNumberFormat(classLoadersList, data);
    }

    /**
     * Register renderer converters.
     *
     * @param classLoadersList the class loaders list
     * @param data the data
     */
    private static void registerRenderers(List<ClassLoader> classLoadersList, ConversionData data) {
        logger.info("Renderers supported:");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.sldeditor.importdata.esri.renderer"))));

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for(Class<? extends Object> claszz : allClasses)
        {
            try {
                if(validClass(claszz, EsriRendererInterface.class))
                {
                    EsriRendererInterface rendererObj = (EsriRendererInterface) claszz.newInstance();
                    logger.info(rendererObj.getRendererClass().getName());
                    data.addRenderer(rendererObj);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Register label converters.
     *
     * @param classLoadersList the class loaders list
     * @param data the data
     */
    private static void registerLabelRenderers(List<ClassLoader> classLoadersList, ConversionData data) {
        logger.info("Label Renderers supported:");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.sldeditor.importdata.esri.label"))));

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for(Class<? extends Object> claszz : allClasses)
        {
            try {
                if(validClass(claszz, EsriLabelRendererInterface.class))
                {
                    EsriLabelRendererInterface rendererObj = (EsriLabelRendererInterface) claszz.newInstance();
                    logger.info(rendererObj.getRendererClass().getName());
                    data.addLabelRenderer(rendererObj);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Register symbols converters.
     *
     * @param classLoadersList the class loaders list
     * @param data the data
     */
    private static void registerSymbols(List<ClassLoader> classLoadersList, ConversionData data) {

        logger.info("Symbols supported:");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.sldeditor.importdata.esri.symbol"))));

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for(Class<? extends Object> claszz : allClasses)
        {
            try {
                if(validClass(claszz, EsriSymbolInterface.class))
                {
                    EsriSymbolInterface symbolObj = (EsriSymbolInterface) claszz.newInstance();
                    logger.info(symbolObj.getSymbolClass().getName());
                    data.addSymbol(symbolObj);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Register text background converters.
     *
     * @param classLoadersList the class loaders list
     * @param data the data
     */
    private static void registerTextBackground(List<ClassLoader> classLoadersList, ConversionData data) {

        logger.info("Text backgrounds supported:");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.sldeditor.importdata.esri.textbackground"))));

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for(Class<? extends Object> claszz : allClasses)
        {
            try {
                if(validClass(claszz, EsriTextBackgroundInterface.class))
                {
                    EsriTextBackgroundInterface textBackgroundObj = (EsriTextBackgroundInterface) claszz.newInstance();
                    logger.info(textBackgroundObj.getBackgroundClass().getName());
                    data.addTextBackground(textBackgroundObj);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Register number format converters.
     *
     * @param classLoadersList the class loaders list
     * @param data the data
     */
    private static void registerNumberFormat(List<ClassLoader> classLoadersList, ConversionData data) {

        logger.info("Number formats supported:");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.sldeditor.importdata.esri.numberformat"))));

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for(Class<? extends Object> claszz : allClasses)
        {
            try {
                if(validClass(claszz, EsriNumberFormatInterface.class))
                {
                    EsriNumberFormatInterface numberFormatObj = (EsriNumberFormatInterface) claszz.newInstance();
                    logger.info(numberFormatObj.getNumberFormatClass().getName());
                    data.addNumberFormat(numberFormatObj);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *Check if a valid class to instantiate
     *
     * @param clazz the class to check
     * @param classToIgnore the class to ignore
     * @return true, if successful
     */
    private static boolean validClass(Class<?> clazz, Class<?> classToIgnore) {
        return ((clazz.getName().compareTo(classToIgnore.getName()) != 0) && (clazz.getSimpleName().compareTo(PACKAGE_INFO) != 0));
    }
}
