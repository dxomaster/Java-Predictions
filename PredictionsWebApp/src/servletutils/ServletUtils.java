package servletutils;

import jakarta.servlet.ServletContext;

public class ServletUtils {
    private static final String SIMULATION_MANAGER_ATTRIBUTE_NAME = "simulationManager";
    //private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private final static Object simulationManagerLock = new Object();
    public static SimulationManager getSimulationManager(ServletContext servletContext) {
        synchronized (simulationManagerLock) {
            if (servletContext.getAttribute(SIMULATION_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SIMULATION_MANAGER_ATTRIBUTE_NAME, new SimulationManager());
            }
        }
        return (SimulationManager) servletContext.getAttribute(SIMULATION_MANAGER_ATTRIBUTE_NAME);
    }
}
