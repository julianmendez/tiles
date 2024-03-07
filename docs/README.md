<head>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/mermaid/9.4.3/mermaid.min.js"> </script>
</head>


# [Tiles](https://julianmendez.github.io/tiles/)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)][license]
[![build](https://github.com/julianmendez/tiles/workflows/Scala%20CI/badge.svg)][build-status]

**Tiles** is a framework to create formal configurations of constraints. Its classes are
written in [Soda][soda] and grouped in packages translated to
[Scala][scala].

The fairness tiles are defined in [package tile][package-tile]
[(Scala translation)][package-tile-scala] and they use entities and other tools defined in
[package tool][package-tool] [(Scala translation)][package-tool-scala].


### Resource Allocation Scenarios

These are some of the implemented fairness tiles for resource allocation scenarios:

| Tile                                                | Class                                       |
|:----------------------------------------------------|:--------------------------------------------|
| all-actor <sub>*(a)*</sub>                          | [AllActorTile][all-actor-tile]              |
| <sub>*(a)*</sub> received <sub>*(m)*</sub>          | [ReceivedSigmaPTile][received-sigma-p-tile] |
| <sub>*(m)*</sub> all-equal <sub>*b*</sub>           | [AllEqualTile][all-equal-tile]              |
| <sub>*(a)*</sub> needed <sub>*(m)*</sub>            | [NeededPTile][needed-p-tile]                |
| <sub>*(m0), (m1)*</sub> all-at-least <sub>*b*</sub> | [AllAtLeastTile][all-at-least-tile]         |
| equality <sub>*b*</sub>                             | [EqualityPipeline][equality-pipeline]       |
| equity <sub>*b*</sub>                               | [EquityPipeline][equity-pipeline]           |

A specific scenario is given as an example
in [ResourceAllocationScenarioExample][resource-allocation-scenario-example].
This scenario is used to test the equality tile
with [EqualityPipelineSpec][equality-pipeline-spec]
and the equity tile
with [EquityPipelineSpec][equity-pipeline-spec].


#### Equality

```mermaid
graph LR;
all-actor --> received;
received --> all-equal;
```


#### Equity

```mermaid
graph LR;
all-actor --> received;
all-actor --> needed;
received --> all-at-least;
needed -->  all-at-least;
```


### Scoring Scenarios

These are some of the implemented fairness tiles for scoring scenarios:

| Tile                                               | Class                                          |
|:---------------------------------------------------|:-----------------------------------------------|
| all-actor <sub>*(a0), (a1), (a2)*</sub>            | [AllActorTripleTile][all-actor-triple-tile]    |
| <sub>*(a)*</sub> prediction <sub>*(m)*</sub>       | [PredictionPTile][prediction-p-tile]           |
| <sub>*(a)*</sub> result <sub>*(m)*</sub>           | [ResultPTile][result-p-tile]                   |
| <sub>*(m0), (m1)*</sub> false-pos <sub>*(m)*</sub> | [FalsePosTile][false-pos-tile]                 |
| <sub>*(a)*</sub> with <sub>*(m)*</sub>             | [WithPTile][with-p-tile]                       |
| <sub>*(m0), (m1)*</sub> correlation <sub>*m*</sub> | [CorrelationTile][correlation-tile]            |
| <sub>*m*</sub> decision <sub>*b*</sub>             | [DecisionTile][decision-tile]                  |
| unbiasedness <sub>*b*</sub>                        | [UnbiasednessPipeline][unbiassedness-pipeline] |

A specific scenario is given as an example in
[ScoringScenarioExample][scoring-scenario-example]. This scenario is used to test the
scoring scenario tile (unbiasedness with respect to falsepositives) with
[UnbiasednessPipelineSpec][unbiassedness-pipeline-spec].


#### Unbiasedness

```mermaid
graph LR;
all-actor --> prediction;
all-actor --> result;
all-actor --> with;
prediction --> false-pos;
result --> false-pos;
with --> correlation;
false-pos --> correlation;
correlation --> decision;
```


### Auxiliary Tiles

The auxiliary tiles are used in the construction of other tiles. Some of the auxiliary tiles
are:

| Tile                                                                     | Class                                   |
|:-------------------------------------------------------------------------|:----------------------------------------|
| <sub>*(a)*</sub> attribute <sub>*(m)*</sub>                              | [AttributePTile][attribute-p-tile]      |
| <sub>*(m0), (m1)*</sub> &sigma; <sub>*(m)*</sub>                         | [SigmaTile][sigma-tile]                 |
| <sub>*(&alpha;0),(&alpha;1)*</sub> zip <sub>*(&alpha;0, &alpha;1)*</sub> | [ZipTile][zip-tile]                     |
| <sub>*(&alpha;0, &alpha;1)*</sub> unzip-0 <sub>*(&alpha;0)*</sub>        | [UnzipPairFstTile][unzip-pair-fst-tile] |
| <sub>*(&alpha;0, &alpha;1)*</sub> unzip-1 <sub>*(&alpha;1)*</sub>        | [UnzipPairSndTile][unzip-pair-snd-tile] |


## Author

[Julian Alfredo Mendez][author]

[author]: https://julianmendez.github.io
[license]: https://www.apache.org/licenses/LICENSE-2.0.txt
[build-status]: https://github.com/julianmendez/tiles/actions
[release-notes]: https://julianmendez.github.io/tiles/RELEASE-NOTES.html
[soda]: https://github.com/julianmendez/soda
[scala]: https://scala-lang.org
[package-tile]: https://github.com/julianmendez/tiles/tree/master/core/src/main/scala/soda/tiles/fairness/tile
[package-tile-scala]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/Package.scala
[package-tool]: https://github.com/julianmendez/tiles/tree/master/core/src/main/scala/soda/tiles/fairness/tool
[package-tool-scala]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tool/Package.scala
[all-actor-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllActorTile.soda
[received-sigma-p-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/ReceivedSigmaPTile.soda
[all-equal-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllEqualTile.soda
[needed-p-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/NeededPTile.soda
[all-at-least-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllAtLeastTile.soda
[equality-pipeline]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/pipeline/EqualityPipeline.soda
[equity-pipeline]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/pipeline/EquityPipeline.soda
[all-actor-triple-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllActorTripleTile.soda
[prediction-p-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/PredictionPTile.soda
[result-p-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/ResultPTile.soda
[false-pos-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/FalsePosTile.soda
[with-p-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/WithPTile.soda
[correlation-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/CorrelationTile.soda
[decision-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/DecisionTile.soda
[unbiassedness-pipeline]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/pipeline/UnbiasednessPipeline.soda
[attribute-p-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AttributePTile.soda
[sigma-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/SigmaTile.soda
[zip-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/ZipTile.soda
[unzip-pair-fst-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/UnzipPairFstTile.soda
[unzip-pair-snd-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/UnzipPairSndTile.soda
[resource-allocation-scenario-example]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/ResourceAllocationScenarioExample.soda
[equality-pipeline-spec]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/EqualityPipelineSpec.soda
[equity-pipeline-spec]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/EquityPipelineSpec.soda
[scoring-scenario-example]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/ScoringScenarioExample.soda
[unbiassedness-pipeline-spec]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/UnbiasednessPipelineSpec.soda

<script>
  window.mermaid.init(undefined, document.querySelectorAll('.language-mermaid'));
</script>


