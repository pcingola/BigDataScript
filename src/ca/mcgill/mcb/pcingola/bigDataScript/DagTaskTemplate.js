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
      { data: { id: '{{threadIdNum}}' } },
      { data: { id: '{{taskIdBase}}', parent: '{{taskThreadNum}}' } },
    ],
    edges: [
      { data: { id: '{{threadDepEdgeId}}', source: '{{threadDepSource}}', target: '{{threadDepTarget}}' } },
      { data: { id: '{{taskDepEdgeId}}', source: '{{taskDepSource}}', target: '{{taskDepTarget}}' } },
    ]
  },
  
  layout: {
    name: 'breadthfirst',
  }
});

}); // on dom ready

