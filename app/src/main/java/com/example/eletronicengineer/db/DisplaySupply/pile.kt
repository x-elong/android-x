package com.example.eletronicengineer.distributionFileSave

import java.io.Serializable

class pile(
    var pileFoundation:pileFoundation,
    var provideCrewLists:List<provideCrewLists>,
    var provideTransportMachines:List<provideTransportMachines>,
    var constructionToolLists:List<constructionToolLists>,
    var implementationRanges:implementationRanges
):Serializable