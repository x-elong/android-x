package com.example.eletronicengineer.db.DisplaySupply

import java.io.Serializable

class Pile(
    var pileFoundation:PileFoundation,
    var provideCrewLists:List<ProvideCrewLists>,
    var provideTransportMachines:List<ProvideTransportMachines>,
    var constructionToolLists:List<ConstructionToolLists>,
    var implementationRanges:ImplementationRanges
):Serializable