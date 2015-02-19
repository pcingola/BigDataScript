$(function(){ // on dom ready

var cy = cytoscape({
  container: document.getElementById('cy'),
  
  style: [
    {
      selector: 'node',
      css: {
        'content': 'data(id)',
        'text-valign': 'center',
        'text-halign': 'center'
      }
    },
    {
      selector: 'edge',
      css: {
        'target-arrow-shape': 'triangle'
      }
    },
    {
      selector: ':selected',
      css: {
        'background-color': 'red',
        'line-color': 'red',
        'target-arrow-color': 'red',
        'source-arrow-color': 'red'
      }
    }
  ],
  
  elements: {
    nodes: [
      { data: { id: '{{threadGraphIdNum}}' } },
      { data: { id: '{{taskGraphName}}', parent: '{{taskGraphThreadNum}}' } },
    ],
    edges: [
      { data: { id: '{{threadGraphEdgeId}}', source: '{{threadGraphEdgeSource}}', target: '{{threadGraphEdgeTarget}}' } },
      { data: { id: '{{taskGraphEdgeName}}', source: '{{taskGraphEdgeSource}}', target: '{{taskGraphEdgeTarget}}' } },
    ]
  },
  
  layout: {
    name: 'breadthfirst',
  }
});

}); // on dom ready

