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
      { data: { id: 'task.line_10.id_82', parent: 'thread_Root' } },
      { data: { id: 'task.line_15.id_83', parent: 'thread_Root' } },
      { data: { id: 'task.line_20.id_84', parent: 'thread_Root' } },
    ],
    edges: [
      { data: { id: 'None-thread_Root', source: 'None', target: 'thread_Root' } },
      { data: { id: 'task.line_10.id_82-task.line_15.id_83', source: 'task.line_10.id_82', target: 'task.line_15.id_83' } },
      { data: { id: 'task.line_10.id_82-task.line_15.id_83', source: 'task.line_10.id_82', target: 'task.line_15.id_83' } },
      { data: { id: 'task.line_15.id_83-task.line_20.id_84', source: 'task.line_15.id_83', target: 'task.line_20.id_84' } },
      { data: { id: 'task.line_15.id_83-task.line_20.id_84', source: 'task.line_15.id_83', target: 'task.line_20.id_84' } },
      { data: { id: 'id-id', source: 'id', target: 'id' } },
    ]
  },
  
  layout: {
    name: 'breadthfirst',
  }
});

}); // on dom ready
