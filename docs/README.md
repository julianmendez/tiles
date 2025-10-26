# [🅃🄸🄻🄴🅂](https://julianmendez.github.io/tiles/)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)][license]
[![build](https://github.com/julianmendez/tiles/workflows/Scala%20CI/badge.svg)][build-status]

**Tiles** is a framework to create formal configurations of constraints. Its classes are
written in [Soda][soda] and grouped in packages translated to
[Scala][scala].

This is an example of a pipeline to detect equality.

![equality](example/equality/equality0.png)

The meaning is: for all agents (`all-agent`), compute how much each agent accumulates (`accumulates`),
and ensure that all of them received the same amount (`all-equal`).
The notation `(a)` denotes a sequence of agents, `(m)` denotes a sequence of quantities, and `b` denotes a single
Boolean value.


## Publications

* <a id="MeKaAlDi2024"></a>
  Julian Alfredo Mendez, Timotheus Kampik, Andrea Aler Tubella, and Virginia Dignum. **A Clearer View on Fairness:
  Visual and Formal Representation for Comparative Analysis**. In Florian Westphal, Einav Peretz-Andersson, Maria
  Riveiro, Kerstin Bach, and Fredrik Heintz, editors, *14th Scandinavian Conference on Artificial Intelligence, SCAI
  2024*, pages 112-120. Swedish Artificial Intelligence Society, June 2024.
  &nbsp; DOI:[10.3384/ecp208013][tiles-doi]
  &nbsp; [Abstract][tiles-abstract]
  &nbsp; [BibTeX][tiles-bibtex]
  &nbsp; [PDF][tiles-pdf]
  &nbsp; [Implementation][tiles-impl]
* <a id="Me2023"></a>
  Julian Alfredo Mendez.
  **Soda: An Object-Oriented Functional Language for Specifying Human-Centered Problems**.
  arXiv
  &nbsp; DOI:[10.48550/arXiv.2310.01961][soda-doi]
  &nbsp; [Abstract][soda-abstract]
  &nbsp; [BibTeX][soda-bibtex]
  &nbsp; [PDF][soda-pdf]
  &nbsp; [Implementation][soda-impl]


## Examples

| Example                                                       | Description                                                                 |
|---------------------------------------------------------------|:----------------------------------------------------------------------------|
|                                                               |                                                                             |
| [Equality][equality-example]                                  | Determines whether every agent receives the same amount of resources.       |
|                                                               |                                                                             |
| [Equity][equity-example]                                      | Determines whether all agents receive resources according to their needs.   |
|                                                               |                                                                             |
| [Envy-Freeness][envy-freeness-example]                        | Determines whether the distribution is envy-free.                           |
|                                                               |                                                                             |
| [Group Fairness][group-fairness-example]                      | Determines whether the distribution preserves fairness across groups.       |
|                                                               |                                                                             |
| [Individual Fairness][individual-fairness-example]            | Determines whether the distribution preserves fairness for individuals.     |
|                                                               |                                                                             |
| [Scoring][scoring-example]                                    | Measures the correlation between false positives and a protected attribute. |
|                                                               |                                                                             |
| [Jain's Index][jains-index-example]                           | Measures fairness according to the Jain's index.                            |
|                                                               |                                                                             |
| [Complement of the Gini Index][complement-gini-index-example] | Measures fairness using the complement of the Gini index.                   |
|                                                               |                                                                             |
| [Child Care Subsidy][ccs-example]                             | Represents a collection of possible pipelines for child care subsidies.     |
|                                                               |                                                                             |


## How it works

See a small explanation of [how Tiles works][how-it-works].


## Executable

This project includes an executable example of [Child Care Subsidy][ccs-example] pipelines.
The script `makeall.sh` creates the file `tiles`, which is an executable JAR file that can be
directly executed in Linux. Its input is a [YAML][yaml] configuration file, like the
[configuration file][test-yaml-conf] provided for the unit tests.


## Author

[Julian Alfredo Mendez][author]

[tiles-doi]: https://doi.org/10.3384/ecp208013

[tiles-abstract]: https://ecp.ep.liu.se/index.php/sais/article/view/1005

[tiles-bibtex]: https://julianmendez.github.io/tiles/bibtex-2024.html

[tiles-pdf]: https://ecp.ep.liu.se/index.php/sais/article/view/1005/913

[tiles-impl]: https://github.com/julianmendez/tiles

[soda-doi]: https://doi.org/10.48550/arXiv.2310.01961

[soda-abstract]: https://arxiv.org/abs/2310.01961

[soda-bibtex]: https://julianmendez.github.io/soda/bibtex-2023.html

[soda-pdf]: https://arxiv.org/pdf/2310.01961

[soda-impl]: https://github.com/julianmendez/soda

[author]: https://julianmendez.github.io

[license]: https://www.apache.org/licenses/LICENSE-2.0.txt

[build-status]: https://github.com/julianmendez/tiles/actions

[release-notes]: https://julianmendez.github.io/tiles/RELEASE-NOTES.html

[soda]: https://github.com/julianmendez/soda

[scala]: https://scala-lang.org

[yaml]: https://yaml.org

[package-tile]: https://github.com/julianmendez/tiles/tree/master/core/src/main/scala/soda/tiles/fairness/tile

[package-tile-scala]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/Package.scala

[package-tool]: https://github.com/julianmendez/tiles/tree/master/core/src/main/scala/soda/tiles/fairness/tool

[package-tool-scala]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tool/Package.scala

[ResourceAllocationScenarioExample]: https://github.com/julianmendez/tiles/blob/master/examples/src/test/scala/soda/tiles/fairness/example/pipeline/equity/ResourceAllocationScenarioExample.soda

[EqualityPipelineSpec]: https://github.com/julianmendez/tiles/blob/master/examples/src/test/scala/soda/tiles/fairness/example/pipeline/equality/EqualityPipelineSpec.soda

[EquityPipelineSpec]: https://github.com/julianmendez/tiles/blob/master/examples/src/test/scala/soda/tiles/fairness/example/pipeline/equity/EquityPipelineSpec.soda

[equality-example]: https://julianmendez.github.io/tiles/example/equality/equality_example.html

[equity-example]: https://julianmendez.github.io/tiles/example/equity/equity_example.html

[envy-freeness-example]: https://julianmendez.github.io/tiles/example/envy_freeness/envy_freeness_example.html

[group-fairness-example]: https://julianmendez.github.io/tiles/example/group_fairness/group_fairness_example.html

[individual-fairness-example]: https://julianmendez.github.io/tiles/example/individual_fairness/individual_fairness_example.html

[jains-index-example]: https://julianmendez.github.io/tiles/example/jains_index/jains_index_example.html

[complement-gini-index-example]: https://julianmendez.github.io/tiles/example/complement_gini_index/complement_gini_index_example.html

[scoring-example]: https://julianmendez.github.io/tiles/example/scoring/scoring_example.html

[ccs-example]: https://julianmendez.github.io/tiles/example/child_care_subsidy/ccs_example.html

[test-yaml-conf]: https://github.com/julianmendez/tiles/blob/master/examples/src/test/resources/example/example0.yaml

[how-it-works]: https://julianmendez.github.io/tiles/tutorial/how_it_works.html


