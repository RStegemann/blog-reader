import {React, Component} from "react";
import BlogPostSelector from "./blogPostSelector";

export default class BlogPostList extends Component{

    render(){
        return (
            <div>
                {this.props.blogPosts.map(blogPost =>
                    <BlogPostSelector
                        key={blogPost.id}
                        blogPost={blogPost}
                        handleSelection={this.props.handleSelection}
                        index = {this.props.blogPosts.indexOf(blogPost)}
                    />)}
            </div>
        )
    }
}