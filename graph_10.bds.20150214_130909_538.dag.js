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
      { data: { id: 'thread_Root' } },
      { data: { id: 'task.line_21.id_101', parent: 'thread_Root' } },
      { data: { id: 'task.line_22.id_102', parent: 'thread_Root' } },
      { data: { id: 'task.line_23.id_103', parent: 'thread_Root' } },
      { data: { id: 'task.line_24.id_104', parent: 'thread_Root' } },
    ],
    edges: [
      { data: { id: 'None-thread_Root', source: 'None', target: 'thread_Root' } },
      { data: { id: 'task.line_21.id_101-task.line_24.id_104', source: 'task.line_21.id_101', target: 'task.line_24.id_104' } },
      { data: { id: 'task.line_21.id_101-task.line_24.id_104', source: 'task.line_21.id_101', target: 'task.line_24.id_104' } },
      { data: { id: 'id-id', source: 'id', target: 'id' } },
    ]
  },
  
  layout: {
    name: 'breadthfirst',
  }
});

}); // on dom ready
