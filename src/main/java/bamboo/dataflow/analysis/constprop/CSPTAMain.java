/*
 * Bamboo - A Program Analysis Framework for Java
 *
 * Copyright (C) 2020 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020 Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * This software is designed for the "Static Program Analysis" course at
 * Nanjing University, and it supports a subset of Java features.
 * Bamboo is only for educational and academic purposes, and any form of
 * commercial use is disallowed.
 */

package bamboo.dataflow.analysis.constprop;

import bamboo.pta.PointerAnalysisTransformer;
import soot.Pack;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

public class CSPTAMain {

    public static void main(String[] args) {
        // Set options
        Options.v().set_src_prec(Options.src_prec_java);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_keep_line_number(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_whole_program(true);
        Options.v().setPhaseOption("cg", "enabled:false");

        // Configure transformer
        Pack wjtp = PackManager.v().getPack("wjtp");
        Transform pta = new Transform("wjtp.pta", PointerAnalysisTransformer.v());
        pta.setDeclaredOptions("enabled cs");
        pta.setDefaultOptions("enabled:true cs:ci");
        wjtp.add(pta);
        Transform cp = new Transform("wjtp.constprop", new IPConstantPropagation());
        cp.setDeclaredOptions("enabled cg");
        cp.setDefaultOptions("enabled:true cg:pta");
        wjtp.add(cp);

        // Run main analysis
        soot.Main.main(args);
    }
}