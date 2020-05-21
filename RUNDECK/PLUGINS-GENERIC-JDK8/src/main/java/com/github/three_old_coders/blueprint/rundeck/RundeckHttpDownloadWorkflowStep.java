package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.github.three_old_coders.blueprint.common.Runner_UrlFileDownloader;
import org.apache.tapestry5.json.JSONObject;
import picocli.CommandLine;

@Plugin(name = RundeckHttpDownloadWorkflowStep.PROVIDER_NAME, service = ServiceNameConstants.WorkflowStep)
public class RundeckHttpDownloadWorkflowStep
    extends RundeckJavaPicoliJsonWorkflowStepPluginBase
{
    public static final String PROVIDER_NAME = "3OC_URL_DOWNLOAD_PLUGIN";
    public static final String DESC = "This workflow step downloads several files in parallel";
    public static final String TITLE = "URL Download Plugin";

    public RundeckHttpDownloadWorkflowStep()
    {
        super(PROVIDER_NAME, TITLE, DESC, createJSON());
    }
    
    private static JSONObject createJSON()
    {
        final CommandLine commandLine = PicoliUtils.createFrom(Runner_UrlFileDownloader.class);
        return PicoliUtils.convert(Runner_UrlFileDownloader.class.getName(), commandLine);
    }
}
