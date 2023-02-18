import {React, Component} from "react";
import BlogPost from "./blogPost";

export default class BlogPostList extends Component{

    render(){
        return (
            <div>
                {this.props.blogPosts.map(blogPost =>
                    <BlogPost
                        key={blogPost.id}
                        blogPost={blogPost}
                    />)}
            </div>
        )
    }
}