plugins {
    alias(libs.plugins.pgt.root)
    alias(libs.plugins.kotlin) apply false
}

preprocess {
    val fabric12004 = createNode("1.20.4-fabric", 12004, "yarn")
    val fabric12100 = createNode("1.21-fabric", 12100, "yarn")

    fabric12100.link(fabric12004)
}
