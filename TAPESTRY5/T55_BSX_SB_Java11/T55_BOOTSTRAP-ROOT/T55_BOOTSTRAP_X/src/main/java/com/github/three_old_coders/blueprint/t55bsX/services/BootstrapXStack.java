package com.github.three_old_coders.blueprint.t55bsX.services;

import com.github.three_old_coders.blueprint.t55bs.services.EBSVersions;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptAggregationStrategy;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BootstrapXStack implements JavaScriptStack
{
	private final List<Asset> _js = new ArrayList<>();
	private final List<StylesheetLink> _css = new ArrayList<>();
	private final List<String> _modules = Collections.emptyList();

	public BootstrapXStack(final AssetSource assetSource, final @Symbol("bootstrap.version") EBSVersions bsVersion)
	{
		if (EBSVersions.v3 == bsVersion) {
			final String PATH_BS_S = "classpath:/META-INF/assets/bootstrap-3.4.1-dist/";
			_js.add(assetSource.getClasspathAsset(PATH_BS_S + "js/bootstrap.min.js"));
			_css.add((new StylesheetLink(assetSource.getClasspathAsset(PATH_BS_S + "css/bootstrap.min.css"))));
			_css.add((new StylesheetLink(assetSource.getClasspathAsset(PATH_BS_S + "css/bootstrap-xlgrid.min.css"))));
		} else if (EBSVersions.v4 == bsVersion) {
			final String PATH_BS_S = "classpath:/META-INF/assets/bootstrap-4.6.0-dist/";
			_js.add(assetSource.getClasspathAsset(PATH_BS_S + "js/bootstrap.bundle.min.js"));
			_css.add((new StylesheetLink(assetSource.getClasspathAsset(PATH_BS_S + "css/bootstrap.min.css"))));
		} else if (EBSVersions.v5 == bsVersion) {
			final String PATH_BS_S = "classpath:/META-INF/assets/bootstrap-5.1.1-dist/";
			_js.add(assetSource.getClasspathAsset(PATH_BS_S + "js/bootstrap.bundle.min.js"));
			_css.add((new StylesheetLink(assetSource.getClasspathAsset(PATH_BS_S + "css/bootstrap.min.css"))));
		} else {
			throw new IllegalStateException("unsupported bootstrap version [" + bsVersion + "]. use [" + Arrays.asList(EBSVersions.values()) + "]");
		}
	}

	@Override public List<String> getStacks()
	{
		return Collections.emptyList();
	}

	@Override public List<Asset> getJavaScriptLibraries()
	{
		return _js;
	}

	@Override public List<StylesheetLink> getStylesheets()
	{
		return _css;
	}

	@Override public List<String> getModules()
	{
		return _modules;
	}

	@Override public JavaScriptAggregationStrategy getJavaScriptAggregationStrategy()
	{
		return JavaScriptAggregationStrategy.COMBINE_AND_MINIMIZE;
	}

	@Override public String getInitialization()
	{
		return "";
	}
}
