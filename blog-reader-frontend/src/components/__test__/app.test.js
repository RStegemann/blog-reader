import {render, screen, cleanup} from '@testing-library/react'
import '@testing-library/jest-dom'
import App from "../app";
test('should render empty component if no posts', () => {
  const posts = []
  render(<App blogPosts={posts}/>);
  const appElement = screen.getByTestId('emptyapp');
  expect(appElement).toBeInTheDocument();
});

test('should render page view if component has posts', () =>{
  const posts = [
    {id:0, title:"title", excerpt:"excerpt", link:"link", content:"content", wordCounts:[["word", 1], ["otherWord", 2]]}
  ]
  render(<App blogPosts={posts}/>);
  const postList = screen.getByTestId('postList');
  const detailView = screen.getByTestId('postDetails');
  const wordCounts = screen.getByTestId('wordCountTable');
  expect(postList).toBeInTheDocument();
  expect(detailView).toBeInTheDocument();
  expect(wordCounts).toBeInTheDocument();
})
