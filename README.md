# dynamic-map
Java implementation of treemapping techniques

Hierarchical data in which leaf nodes have associated attributes appear in several applications. Rectangular Treemaps (RTs) were designed to display this type of data by tightly packing cells representing the tree nodes, with additional information stored in the cell sizes and/or colors. For dynamic hierarchies, RTs have to obey several requirements regarding the optimal aspect ratio of cells, but also the stability of layout in presence of data changes. While static RTs studied the first requirement well, far less information is available on how RTs behave both requirements for dynamic data. We study how four known RT methods compare over several real-world dynamic hierarchies, and highlight how recent RT methods can be adapted to effectively handle optimal cell ratios and stability.

This repository has the implementation of four treemapping techniques along with their variations: 
### Squarified Treemap
### Slice-and-Dice
### Ordered Treemap
### Nmap
### Strip Treemap
### Spiral Treemap


BRULS M., HUIZING K., VAN WIJK J.: Squarified treemaps.
In In Proceedings of the Joint Eurographics and IEEE TCVG Symposium
on Visualization (1999), Press, pp. 33–42.

DUARTE F. S. L. G., SIKANSI F., FATORE F. M., FADEL S. G., PAULOVICH F. V.: Nmap: A novel neighborhood preservation space-filling algorithm.
IEEE Transactions on Visualization and Computer Graphics 20, 12 (Dec 2014), 2063–2071.

SHNEIDERMAN B.: Tree visualization with tree-maps: 2-d space-filling approach.
ACM Trans. Graph. 11, 1 (Jan. 1992), 92–99.

SHNEIDERMAN B., WATTENBERG M.: Ordered treemap layouts. 
In Proceedings of the IEEE Symposium on Information Visualization 2001 (INFOVIS’01) (Washington, DC, USA, 2001), INFOVIS ’01,
IEEE Computer Society, pp. 73–.
