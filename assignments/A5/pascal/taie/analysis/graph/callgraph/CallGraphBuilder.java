/*
 * Tai-e: A Static Analysis Framework for Java
 *
 * Copyright (C) 2020-- Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020-- Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * Tai-e is only for educational and academic purposes,
 * and any form of commercial use is disallowed.
 * Distribution of Tai-e is disallowed without the approval.
 */

package pascal.taie.analysis.graph.callgraph;

import pascal.taie.analysis.ProgramAnalysis;
import pascal.taie.config.AnalysisConfig;
import pascal.taie.config.ConfigException;
import pascal.taie.ir.stmt.Invoke;
import pascal.taie.language.classes.JMethod;

public class CallGraphBuilder extends ProgramAnalysis {

    public static final String ID = "cg";

    private final String algorithm;

    public CallGraphBuilder(AnalysisConfig config) {
        super(config);
        algorithm = config.getOptions().getString("algorithm");
    }

    @Override
    public CallGraph<Invoke, JMethod> analyze() {
        CGBuilder<Invoke, JMethod> builder = switch (algorithm) {
            case "pta", "cipta", "cspta" -> new PTABasedBuilder(algorithm);
            default -> throw new ConfigException(
                    "Unknown call graph building algorithm: " + algorithm);
        };
        CallGraph<Invoke, JMethod> callGraph = builder.build();
        takeAction(callGraph);
        return callGraph;
    }

    private void takeAction(CallGraph<Invoke, JMethod> callGraph) {
        String action = getOptions().getString("action");
        if (action == null) {
            return;
        }
        if (action.equals("dump")) {
            String file = getOptions().getString("file");
            CallGraphs.dumpCallGraph(callGraph, file);
        }
    }
}