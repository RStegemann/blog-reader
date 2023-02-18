import {React, Component} from "react";

class BlogPostList extends Component{
    state = {
        blogPosts: []
    }

    render(){
        return (
            <div>
                <ul>{this.state.blogPosts.map(blogPost => <li key={blogPost.id}>{blogPost.title}</li>)}</ul>
            </div>
        )
    }
}