package de._3oc_.t55bs4.services;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptAggregationStrategy;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bootstrap4Stack implements JavaScriptStack
{
	public static final String BOOTSTRAP = "bootstrap-4.0.0-dist";

	private static final String PATH_BS4_S = "/META-INF/assets/" + BOOTSTRAP + "/";

	private final List<Asset> _js = new ArrayList<>();
	private final List<StylesheetLink> _css = new ArrayList<>();
	private final List<String> _modules = Collections.emptyList();

	public Bootstrap4Stack(final AssetSource assetSource)
	{
		_js.add(assetSource.getClasspathAsset(PATH_BS4_S + "js/bootstrap.bundle.min.js"));
		_css.add((new StylesheetLink(assetSource.getClasspathAsset(PATH_BS4_S + "css/bootstrap.min.css"))));
	}

	@Override
	public List<String> getStacks()
	{
		return Collections.emptyList();
	}

	@Override
	public List<Asset> getJavaScriptLibraries()
	{
		return _js;
	}

	@Override
	public List<StylesheetLink> getStylesheets()
	{
		return _css;
	}

	@Override
	public List<String> getModules()
	{
		return _modules;
	}

	@Override
	public JavaScriptAggregationStrategy getJavaScriptAggregationStrategy()
	{
		return JavaScriptAggregationStrategy.COMBINE_AND_MINIMIZE;
	}

	@Override
	public String getInitialization()
	{
		return "";
	}
}
